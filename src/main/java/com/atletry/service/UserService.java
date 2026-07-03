package com.atletry.service;

import com.atletry.dto.request.CompleteProfileRequest;
import com.atletry.dto.request.SelectSportsRequest;
import com.atletry.dto.request.UpdateUserSportRequest;
import com.atletry.dto.response.UserNotificationResponse;
import com.atletry.dto.response.UserResponse;
import com.atletry.dto.response.UserSportResponse;
import com.atletry.entity.Role;
import com.atletry.entity.Sport;
import com.atletry.entity.User;
import com.atletry.entity.UserNotification;
import com.atletry.entity.UserSport;
import com.atletry.enums.MediaUploadEntityType;
import com.atletry.enums.RoleName;
import com.atletry.enums.SkillLevel;
import com.atletry.enums.SportActivityAction;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.mapper.UserMapper;
import com.atletry.repository.RoleRepository;
import com.atletry.repository.UserNotificationRepository;
import com.atletry.repository.UserRepository;
import com.atletry.repository.UserSportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository             userRepo;
    private final RoleRepository             roleRepo;
    private final UserSportRepository        userSportRepo;
    private final UserNotificationRepository notificationRepo;
    private final SportService               sportService;
    private final S3LogService               s3LogService;
    private final MediaUploadService         mediaUploadService;
    private final UserMapper                 userMapper;

    // ── Profile ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public UserResponse getProfile(User user) {
        List<UserSportResponse> sports = userMapper.toUserSportResponseList(
                userSportRepo.findByUserId(user.getId()));
        UserResponse res = userMapper.toResponse(user);
        res.setSports(sports);
        return res;
    }

    @Transactional
    public UserResponse completeProfile(User user, CompleteProfileRequest req, MultipartFile image) {
        user.setName(req.getName());
        user.setCity(req.getCity());
        user.setLatitude(req.getLatitude());
        user.setLongitude(req.getLongitude());
        userRepo.save(user);

        if (image != null && !image.isEmpty()) {
            String url = mediaUploadService.uploadAndRecord(image, MediaUploadEntityType.USER, user.getId(), user);
            user.setProfileImageUrl(url);
            userRepo.save(user);
        }

        return getProfile(user);
    }

    @Transactional
    public UserResponse uploadProfileImage(User user, MultipartFile image) {
        String url = mediaUploadService.uploadAndRecord(image, MediaUploadEntityType.USER, user.getId(), user);
        user.setProfileImageUrl(url);
        userRepo.save(user);
        return getProfile(user);
    }

    // ── Sport selections ─────────────────────────────────────────────────────

    /**
     * Bulk-select sports (onboarding).  Replaces all existing selections.
     * Sets profileComplete = true when name + city are also already filled.
     */
    @Transactional
    public List<UserSportResponse> selectSports(User user, SelectSportsRequest req) {
        if (req.getSports().size() < 3) {
            throw new BadRequestException("Please select at least 3 sports.");
        }

        userSportRepo.deleteAllByUserId(user.getId());

        List<UserSport> saved = req.getSports().stream().map(item -> {
            Sport sport = sportService.findOrThrow(item.getSportId());
            UserSport us = userSportRepo.save(UserSport.builder()
                    .user(user).sport(sport).skillLevel(item.getSkillLevel()).build());
            s3LogService.log(user, sport, SportActivityAction.SPORT_SELECTED,
                    Map.of("skillLevel", item.getSkillLevel().name()));
            return us;
        }).toList();

        // Mark profile complete once both name/city and sports are set
        if (user.getName() != null && !user.getName().isBlank()) {
            user.setProfileComplete(true);
            userRepo.save(user);
        }

        return userMapper.toUserSportResponseList(saved);
    }

    /** Add a single sport (post-onboarding). */
    @Transactional
    public UserSportResponse addSport(User user, Long sportId, SkillLevel skillLevel) {
        if (userSportRepo.existsByUserIdAndSportId(user.getId(), sportId)) {
            throw new BadRequestException("You have already added this sport.");
        }
        Sport sport = sportService.findOrThrow(sportId);
        UserSport us = userSportRepo.save(UserSport.builder()
                .user(user).sport(sport).skillLevel(skillLevel).build());
        s3LogService.log(user, sport, SportActivityAction.SPORT_SELECTED,
                Map.of("skillLevel", skillLevel.name()));
        return userMapper.toUserSportResponse(us);
    }

    /** Change skill level for an existing user-sport. */
    @Transactional
    public UserSportResponse updateSkillLevel(User user, Long userSportId,
                                              UpdateUserSportRequest req) {
        UserSport us = userSportRepo.findById(userSportId)
                .filter(u -> u.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Sport selection not found."));

        us.setSkillLevel(req.getSkillLevel());
        UserSport saved = userSportRepo.save(us);

        s3LogService.log(user, saved.getSport(), SportActivityAction.LEVEL_CHANGED,
                Map.of("newLevel", req.getSkillLevel().name()));

        return userMapper.toUserSportResponse(saved);
    }

    /** Remove a sport — minimum 3 must remain. */
    @Transactional
    public void removeSport(User user, Long userSportId) {
        UserSport us = userSportRepo.findById(userSportId)
                .filter(u -> u.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Sport selection not found."));

        if (userSportRepo.countByUserId(user.getId()) <= 3) {
            throw new BadRequestException("You must keep at least 3 sports.");
        }

        s3LogService.log(user, us.getSport(), SportActivityAction.SPORT_REMOVED, null);
        userSportRepo.delete(us);
    }

    @Transactional
    public void grantAdmin(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (user.hasRole(RoleName.SUPER_ADMIN)) {
            throw new BadRequestException("Cannot change the role of a super-admin");
        }
        Role adminRole = roleRepo.findByName(RoleName.ADMIN).orElseThrow();
        user.getRoles().add(adminRole);
        userRepo.save(user);
    }

    @Transactional
    public void revokeAdmin(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (user.hasRole(RoleName.SUPER_ADMIN)) {
            throw new BadRequestException("Cannot change the role of a super-admin");
        }
        Role adminRole = roleRepo.findByName(RoleName.ADMIN).orElseThrow();
        user.getRoles().remove(adminRole);
        userRepo.save(user);
    }

    // ── Notifications ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<UserNotificationResponse> getNotifications(User user) {
        return notificationRepo.findByUserIdOrderBySentAtDesc(user.getId())
                .stream()
                .map(this::toNotificationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {
        return notificationRepo.countByUserIdAndIsReadFalse(user.getId());
    }

    @Transactional
    public void markAsRead(User user, Long notificationId) {
        int updated = notificationRepo.markAsRead(notificationId, user.getId());
        if (updated == 0) {
            throw new ResourceNotFoundException("Notification not found");
        }
    }

    @Transactional
    public void markAllAsRead(User user) {
        notificationRepo.markAllAsRead(user.getId());
    }

    private UserNotificationResponse toNotificationResponse(UserNotification n) {
        return UserNotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .body(n.getBody())
                .eventType(n.getEventType().name())
                .isRead(n.isRead())
                .sentAt(n.getSentAt())
                .build();
    }
}
