package com.atletry.service;

import com.atletry.dto.request.CreateSportRequest;
import com.atletry.dto.request.UpdateSportRequest;
import com.atletry.dto.response.SportResponse;
import com.atletry.entity.SkillLevel;
import com.atletry.entity.Sport;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.mapper.SportMapper;
import com.atletry.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository  sportRepo;
    private final SportMapper      sportMapper;
    private final S3UploadService  s3UploadService;

    @Transactional(readOnly = true)
    public List<SportResponse> getAllActive() {
        return sportMapper.toResponseList(
                sportRepo.findByIsActiveTrueOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public SportResponse getById(Long id) {
        return sportMapper.toResponse(findOrThrow(id));
    }


    @Transactional
    public SportResponse create(CreateSportRequest req, MultipartFile icon) {
        if (sportRepo.existsByNameIgnoreCase(req.getName())) {
            throw new BadRequestException("Sport '" + req.getName() + "' already exists.");
        }

        String iconUrl = (icon != null && !icon.isEmpty())
                ? s3UploadService.uploadSportIcon(icon)
                : null;

        Sport sport = Sport.builder()
                .name(req.getName())
                .description(req.getDescription())
                .iconUrl(iconUrl)
                .displayOrder(req.getDisplayOrder())
                .build();

        Arrays.stream(com.atletry.enums.SkillLevel.values()).forEach(lvl ->
                sport.getSkillLevels().add(SkillLevel.builder()
                        .sport(sport)
                        .levelCode(lvl)
                        .label(lvl.getLabel())
                        .description(lvl.getDescription())
                        .build()));

        return sportMapper.toResponse(sportRepo.save(sport));
    }

    @Transactional
    public SportResponse update(Long id, UpdateSportRequest req) {
        Sport sport = findOrThrow(id);

        if (req.getName() != null) {
            if (!req.getName().equalsIgnoreCase(sport.getName())
                    && sportRepo.existsByNameIgnoreCase(req.getName())) {
                throw new BadRequestException("Sport '" + req.getName() + "' already exists.");
            }
            sport.setName(req.getName());
        }
        if (req.getDescription()  != null) sport.setDescription(req.getDescription());
        if (req.getIconUrl()      != null) sport.setIconUrl(req.getIconUrl());
        if (req.getDisplayOrder() != null) sport.setDisplayOrder(req.getDisplayOrder());
        if (req.getIsActive()     != null) sport.setActive(req.getIsActive());

        return sportMapper.toResponse(sportRepo.save(sport));
    }

    @Transactional
    public void delete(Long id) {
        Sport sport = findOrThrow(id);
        sport.setActive(false);
        sportRepo.save(sport);
    }

    public Sport findOrThrow(Long id) {
        return sportRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found: " + id));
    }
}
