package com.atletry.controller;

import com.atletry.dto.request.CreateNotificationTemplateRequest;
import com.atletry.dto.request.UpdateNotificationTemplateRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.NotificationTemplateResponse;
import com.atletry.enums.NotificationEventType;
import com.atletry.service.NotificationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/notification-templates")
@RequiredArgsConstructor
@Tag(name = "7. Notification Templates", description = "Manage push notification templates bound to platform events")
public class NotificationTemplateController {

    private final NotificationTemplateService templateService;

    @GetMapping
    @Operation(summary = "List all notification templates")
    ResponseEntity<ApiResponse<List<NotificationTemplateResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Templates fetched", templateService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a template by ID")
    ResponseEntity<ApiResponse<NotificationTemplateResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Template fetched", templateService.getById(id)));
    }

    @GetMapping("/event/{eventType}")
    @Operation(summary = "Get a template by event type")
    ResponseEntity<ApiResponse<NotificationTemplateResponse>> getByEventType(
            @Parameter(description = "One of: MATCH_CREATED, TOURNAMENT_CREATED, COACH_ADDED, GROUND_ADDED")
            @PathVariable NotificationEventType eventType) {
        return ResponseEntity.ok(ApiResponse.ok("Template fetched", templateService.getByEventType(eventType)));
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a notification template",
               description = "One template per event type. Use {sportName}, {title}, {name}, {venue}, {creatorName} as placeholders.")
    ResponseEntity<ApiResponse<NotificationTemplateResponse>> create(
            @Valid @RequestBody CreateNotificationTemplateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Template created", templateService.create(req)));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a notification template", description = "Partial update — only provided fields are changed.")
    ResponseEntity<ApiResponse<NotificationTemplateResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationTemplateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Template updated", templateService.update(id, req)));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a notification template")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        templateService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Template deleted"));
    }
}
