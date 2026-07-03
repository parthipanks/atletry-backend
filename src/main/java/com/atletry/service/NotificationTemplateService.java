package com.atletry.service;

import com.atletry.dto.request.CreateNotificationTemplateRequest;
import com.atletry.dto.request.UpdateNotificationTemplateRequest;
import com.atletry.dto.response.NotificationTemplateResponse;
import com.atletry.entity.NotificationTemplate;
import com.atletry.enums.NotificationEventType;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;

    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getAll() {
        return templateRepository.findAllByOrderByCreatedDateDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationTemplateResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public NotificationTemplateResponse getByEventType(NotificationEventType eventType) {
        return toResponse(
                templateRepository.findByEventType(eventType)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "No template found for event: " + eventType)));
    }

    @Transactional
    public NotificationTemplateResponse create(CreateNotificationTemplateRequest req) {
        if (templateRepository.existsByEventType(req.getEventType())) {
            throw new BadRequestException(
                    "A template already exists for event: " + req.getEventType()
                    + ". Use PUT to update it.");
        }
        NotificationTemplate template = NotificationTemplate.builder()
                .eventType(req.getEventType())
                .title(req.getTitle())
                .body(req.getBody())
                .description(req.getDescription())
                .build();
        return toResponse(templateRepository.save(template));
    }

    @Transactional
    public NotificationTemplateResponse update(Long id, UpdateNotificationTemplateRequest req) {
        NotificationTemplate template = findOrThrow(id);

        if (req.getEventType() != null && !req.getEventType().equals(template.getEventType())) {
            if (templateRepository.existsByEventType(req.getEventType())) {
                throw new BadRequestException(
                        "A template already exists for event: " + req.getEventType());
            }
            template.setEventType(req.getEventType());
        }
        if (req.getTitle()       != null) template.setTitle(req.getTitle());
        if (req.getBody()        != null) template.setBody(req.getBody());
        if (req.getDescription() != null) template.setDescription(req.getDescription());
        if (req.getIsActive()    != null) template.setActive(req.getIsActive());

        return toResponse(templateRepository.save(template));
    }

    @Transactional
    public void delete(Long id) {
        templateRepository.delete(findOrThrow(id));
    }

    public NotificationTemplate findOrThrow(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification template not found: " + id));
    }

    private NotificationTemplateResponse toResponse(NotificationTemplate t) {
        return NotificationTemplateResponse.builder()
                .id(t.getId())
                .eventType(t.getEventType().name())
                .title(t.getTitle())
                .body(t.getBody())
                .description(t.getDescription())
                .isActive(t.isActive())
                .createdDate(t.getCreatedDate())
                .updatedDate(t.getUpdatedDate())
                .build();
    }
}
