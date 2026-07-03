package com.atletry.controller;

import com.atletry.dto.request.*;
import com.atletry.dto.response.*;
import com.atletry.entity.User;
import com.atletry.service.GroundService;
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
@RequestMapping("/grounds")
@RequiredArgsConstructor
@Tag(name = "3. Grounds", description = "Venue registration — browse, register, manage courts, hours and amenities")
public class GroundController {

    private final GroundService groundService;

    // ── Browse ────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all active venues", description = "No token required.")
    public ResponseEntity<ApiResponse<List<GroundResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Grounds fetched", groundService.getAllActive()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List venues awaiting approval  [ADMIN only]")
    public ResponseEntity<ApiResponse<List<GroundResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending grounds", groundService.getPendingApproval()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get venue by ID", description = "No token required.")
    public ResponseEntity<ApiResponse<GroundResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Ground fetched", groundService.getById(id)));
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List my registered venues")
    public ResponseEntity<ApiResponse<List<GroundResponse>>> getMyGrounds(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Your grounds", groundService.getMyGrounds(user)));
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Register a venue",
               description = "Send `data` part as JSON and optional `images` parts for cover photos (multiple files allowed). "
                           + "Courts and operating hours can be included in the JSON or added later via their own endpoints.")
    public ResponseEntity<ApiResponse<GroundResponse>> create(
            @RequestPart("data") @Valid CreateGroundRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Ground registered", groundService.create(req, user, images)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update venue details",
               description = "Only the registering user can update. Send `data` part as JSON and optional `images` parts to add more photos.")
    public ResponseEntity<ApiResponse<GroundResponse>> update(
            @PathVariable Long id,
            @RequestPart("data") @Valid UpdateGroundRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Ground updated", groundService.update(id, req, user, images)));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Soft-delete a venue", description = "Only the registering user can delete.")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        groundService.delete(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Ground deactivated"));
    }

    // ── Courts ────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/courts")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a court to a venue")
    public ResponseEntity<ApiResponse<CourtResponse>> addCourt(
            @PathVariable Long id,
            @RequestBody @Valid CreateCourtRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Court added", groundService.addCourt(id, req, user)));
    }

    @PutMapping("/{id}/courts/{courtId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a court", description = "Partial update — only provided fields change.")
    public ResponseEntity<ApiResponse<CourtResponse>> updateCourt(
            @PathVariable Long id,
            @PathVariable Long courtId,
            @RequestBody @Valid UpdateCourtRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Court updated",
                groundService.updateCourt(id, courtId, req, user)));
    }

    @DeleteMapping("/{id}/courts/{courtId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a court", description = "Soft-delete — existing pricing tiers are preserved.")
    public ResponseEntity<ApiResponse<Void>> removeCourt(
            @PathVariable Long id,
            @PathVariable Long courtId,
            @AuthenticationPrincipal User user) {
        groundService.removeCourt(id, courtId, user);
        return ResponseEntity.ok(ApiResponse.ok("Court removed"));
    }

    // ── Pricing tiers ─────────────────────────────────────────────────────────

    @PostMapping("/{id}/courts/{courtId}/pricing")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a pricing tier to a court")
    public ResponseEntity<ApiResponse<PricingTierResponse>> addPricingTier(
            @PathVariable Long id,
            @PathVariable Long courtId,
            @RequestBody @Valid CreatePricingTierRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pricing tier added",
                        groundService.addPricingTier(id, courtId, req, user)));
    }

    @PutMapping("/{id}/courts/{courtId}/pricing/{tierId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a pricing tier")
    public ResponseEntity<ApiResponse<PricingTierResponse>> updatePricingTier(
            @PathVariable Long id,
            @PathVariable Long courtId,
            @PathVariable Long tierId,
            @RequestBody @Valid UpdatePricingTierRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Pricing tier updated",
                groundService.updatePricingTier(id, courtId, tierId, req, user)));
    }

    @DeleteMapping("/{id}/courts/{courtId}/pricing/{tierId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a pricing tier")
    public ResponseEntity<ApiResponse<Void>> removePricingTier(
            @PathVariable Long id,
            @PathVariable Long courtId,
            @PathVariable Long tierId,
            @AuthenticationPrincipal User user) {
        groundService.removePricingTier(id, courtId, tierId, user);
        return ResponseEntity.ok(ApiResponse.ok("Pricing tier removed"));
    }

    // ── Operating hours ───────────────────────────────────────────────────────

    @PutMapping("/{id}/operating-hours")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Set / replace operating hours",
               description = "Upserts hours for each day supplied. Days not supplied are left unchanged.")
    public ResponseEntity<ApiResponse<List<OperatingHoursResponse>>> setOperatingHours(
            @PathVariable Long id,
            @RequestBody @Valid List<OperatingHoursRequest> req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Operating hours updated",
                groundService.setOperatingHours(id, req, user)));
    }
}
