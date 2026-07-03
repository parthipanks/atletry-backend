package com.atletry.controller;

import com.atletry.dto.request.CompleteProfileRequest;
import com.atletry.dto.request.SelectSportsRequest;
import com.atletry.dto.request.UpdateUserSportRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.UserNotificationResponse;
import com.atletry.dto.response.UserResponse;
import com.atletry.dto.response.UserSportResponse;
import com.atletry.entity.User;
import com.atletry.enums.SkillLevel;
import com.atletry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "3. User Profile & Sports", description = "Profile setup and sport preference management — JWT required")
public class UserController {

    private final UserService userService;


    @GetMapping
    @Operation(
        summary = "Get my profile",
        description = "Returns the authenticated user's profile including all selected sports."
    )
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched", userService.getProfile(user)));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Complete / update profile",
        description = """
            Set the user's **name** and **city**. Call this after the first login.
            Send `data` part as JSON (`application/json`) and an optional `image` part for the profile photo.

            Tip: pass `latitude` and `longitude` from the device GPS and resolve city on the client
            (reverse-geocoding) before calling this endpoint, or let the backend resolve it later.
            """
    )
    public ResponseEntity<ApiResponse<UserResponse>> completeProfile(
            @AuthenticationPrincipal User user,
            @RequestPart("data") @Valid CompleteProfileRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(
                ApiResponse.ok("Profile updated", userService.completeProfile(user, req, image)));
    }

    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload / replace profile photo",
        description = "Upload a JPEG, PNG, WebP, or SVG image (max 5 MB) as the user's profile picture."
    )
    public ResponseEntity<ApiResponse<UserResponse>> uploadProfileImage(
            @AuthenticationPrincipal User user,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(
                ApiResponse.ok("Profile image updated", userService.uploadProfileImage(user, image)));
    }


    @PostMapping("/sports")
    @Operation(
        summary = "Bulk-select sports (onboarding)",
        description = """
            **Replaces** all existing sport selections with the provided list.
            Minimum **3 sports** required. Each must include a `skillLevel`:

            | Code | Label |
            |------|-------|
            | `JUST_STARTING` | Just Starting |
            | `INTERMEDIATE`  | Intermediate  |
            | `COMPETITIVE`   | Competitive   |

            Once name/city are also set, `profileComplete` becomes `true`.
            """
    )
    public ResponseEntity<ApiResponse<List<UserSportResponse>>> selectSports(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SelectSportsRequest req) {
        return ResponseEntity.ok(
                ApiResponse.ok("Sports saved", userService.selectSports(user, req)));
    }

    @PostMapping("/sports/{sportId}")
    @Operation(
        summary = "Add a single sport",
        description = "Add one sport with a chosen skill level. Use after onboarding to expand the list."
    )
    public ResponseEntity<ApiResponse<UserSportResponse>> addSport(
            @AuthenticationPrincipal User user,
            @PathVariable Long sportId,
            @Parameter(description = "Chosen skill level", example = "JUST_STARTING")
            @RequestParam SkillLevel skillLevel) {
        return ResponseEntity.ok(
                ApiResponse.ok("Sport added", userService.addSport(user, sportId, skillLevel)));
    }

    @PatchMapping("/sports/{userSportId}")
    @Operation(
        summary = "Update skill level",
        description = "Change the skill level for an already-selected sport."
    )
    public ResponseEntity<ApiResponse<UserSportResponse>> updateSkillLevel(
            @AuthenticationPrincipal User user,
            @PathVariable Long userSportId,
            @Valid @RequestBody UpdateUserSportRequest req) {
        return ResponseEntity.ok(
                ApiResponse.ok("Skill level updated",
                        userService.updateSkillLevel(user, userSportId, req)));
    }

    @DeleteMapping("/sports/{userSportId}")
    @Operation(
        summary = "Remove a sport",
        description = "Remove a sport from the user's list. **Minimum 3 must remain.**"
    )
    public ResponseEntity<ApiResponse<Void>> removeSport(
            @AuthenticationPrincipal User user,
            @PathVariable Long userSportId) {
        userService.removeSport(user, userSportId);
        return ResponseEntity.ok(ApiResponse.ok("Sport removed"));
    }

    // ── Notifications ─────────────────────────────────────────────────────────

    @GetMapping("/notifications")
    @Operation(summary = "List my notifications", description = "Returns all notifications for the authenticated user, newest first.")
    public ResponseEntity<ApiResponse<List<UserNotificationResponse>>> getNotifications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Notifications fetched", userService.getNotifications(user)));
    }

    @GetMapping("/notifications/unread-count")
    @Operation(summary = "Unread notification count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Unread count", userService.getUnreadCount(user)));
    }

    @PatchMapping("/notifications/{id}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        userService.markAsRead(user, id);
        return ResponseEntity.ok(ApiResponse.ok("Notification marked as read"));
    }

    @PatchMapping("/notifications/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal User user) {
        userService.markAllAsRead(user);
        return ResponseEntity.ok(ApiResponse.ok("All notifications marked as read"));
    }
}
