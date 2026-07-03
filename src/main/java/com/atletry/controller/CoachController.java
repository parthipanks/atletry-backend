package com.atletry.controller;

import com.atletry.dto.request.*;
import com.atletry.dto.response.*;
import com.atletry.entity.User;
import com.atletry.service.CoachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/coaches")
@RequiredArgsConstructor
@Tag(name = "4. Coaches", description = "Coach management — browse, register, programs, availability, assignments")
public class CoachController {

    private final CoachService coachService;

    // ── Browse ────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all approved coaches", description = "No token required.")
    public ResponseEntity<ApiResponse<List<CoachResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Coaches fetched", coachService.getAllActive()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List coaches awaiting approval  [ADMIN only]")
    public ResponseEntity<ApiResponse<List<CoachResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending coaches", coachService.getPendingApproval()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coach by ID", description = "No token required.")
    public ResponseEntity<ApiResponse<CoachResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Coach fetched", coachService.getById(id)));
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List my assigned coaches")
    public ResponseEntity<ApiResponse<List<UserCoachResponse>>> getMyCoaches(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Your coaches", coachService.getMyCoaches(user)));
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Register a coach profile",
               description = "Send `data` as JSON and optional `images` for profile (index 0) and cover (index 1) photos.")
    public ResponseEntity<ApiResponse<CoachResponse>> create(
            @RequestPart("data") @Valid CreateCoachRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Coach registered", coachService.create(req, images, currentUser)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update coach profile",
               description = "Partial update — only provided fields change. Optional `images` replaces profile/cover photos.")
    public ResponseEntity<ApiResponse<CoachResponse>> update(
            @PathVariable Long id,
            @RequestPart("data") @Valid UpdateCoachRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("Coach updated", coachService.update(id, req, images, currentUser)));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deactivate a coach", description = "Soft-delete — existing assignments are preserved.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        coachService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Coach deactivated"));
    }

    // ── Approval ──────────────────────────────────────────────────────────────

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Approve a coach  [ADMIN only]")
    public ResponseEntity<ApiResponse<CoachResponse>> approve(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Coach approved", coachService.approve(id)));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Reject a coach  [ADMIN only]")
    public ResponseEntity<ApiResponse<CoachResponse>> reject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Coach rejected", coachService.reject(id)));
    }

    // ── Programs ──────────────────────────────────────────────────────────────

    @PostMapping(value = "/{id}/programs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a program to a coach profile",
               description = "Send `data` as JSON and optional `images` for program photos.")
    public ResponseEntity<ApiResponse<CoachProgramResponse>> addProgram(
            @PathVariable Long id,
            @RequestPart("data") @Valid CoachProgramRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Program added", coachService.addProgram(id, req, images, currentUser)));
    }

    @PutMapping(value = "/{id}/programs/{programId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a program",
               description = "Partial update. New `images` are appended to existing program photos.")
    public ResponseEntity<ApiResponse<CoachProgramResponse>> updateProgram(
            @PathVariable Long id,
            @PathVariable Long programId,
            @RequestPart("data") @Valid CoachProgramRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("Program updated",
                coachService.updateProgram(id, programId, req, images, currentUser)));
    }

    @DeleteMapping("/{id}/programs/{programId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a program", description = "Soft-delete.")
    public ResponseEntity<ApiResponse<Void>> removeProgram(
            @PathVariable Long id,
            @PathVariable Long programId,
            @AuthenticationPrincipal User currentUser) {
        coachService.removeProgram(id, programId, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Program removed"));
    }

    // ── Weekly availability ───────────────────────────────────────────────────

    @PutMapping("/{id}/availability")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Set weekly availability",
               description = "Upserts slots for each day supplied. Days not supplied are left unchanged.")
    public ResponseEntity<ApiResponse<List<CoachWeeklySlotResponse>>> setAvailability(
            @PathVariable Long id,
            @RequestBody @Valid List<WeeklySlotRequest> slots,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("Availability updated",
                coachService.setWeeklyAvailability(id, slots, currentUser)));
    }

    // ── Blocked dates ─────────────────────────────────────────────────────────

    @PostMapping("/{id}/blocked-dates")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a blocked date")
    public ResponseEntity<ApiResponse<CoachBlockedDateResponse>> addBlockedDate(
            @PathVariable Long id,
            @RequestBody @Valid BlockedDateRequest req,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Blocked date added",
                        coachService.addBlockedDate(id, req, currentUser)));
    }

    @DeleteMapping("/{id}/blocked-dates/{dateId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a blocked date")
    public ResponseEntity<ApiResponse<Void>> removeBlockedDate(
            @PathVariable Long id,
            @PathVariable Long dateId,
            @AuthenticationPrincipal User currentUser) {
        coachService.removeBlockedDate(id, dateId, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Blocked date removed"));
    }

    // ── User–coach assignments ────────────────────────────────────────────────

    @PostMapping("/{coachId}/assign")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Assign me to a coach")
    public ResponseEntity<ApiResponse<UserCoachResponse>> assign(
            @PathVariable Long coachId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Assigned to coach", coachService.assignUser(coachId, user)));
    }

    @DeleteMapping("/{coachId}/assign")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Unassign me from a coach")
    public ResponseEntity<ApiResponse<Void>> unassign(
            @PathVariable Long coachId,
            @AuthenticationPrincipal User user) {
        coachService.unassignUser(coachId, user);
        return ResponseEntity.ok(ApiResponse.ok("Unassigned from coach"));
    }
}
