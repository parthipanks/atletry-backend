package com.atletry.controller;

import com.atletry.dto.request.*;
import com.atletry.dto.response.*;
import com.atletry.entity.User;
import com.atletry.service.AcademyService;
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
@RequestMapping("/academies")
@RequiredArgsConstructor
@Tag(name = "5. Academies", description = "Academy management — profile, branches, staff, batches, fees, approval")
public class AcademyController {

    private final AcademyService academyService;

    // ── Browse ────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all published academies", description = "No token required.")
    public ResponseEntity<ApiResponse<List<AcademyResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Academies fetched", academyService.getAllPublished()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List academies awaiting approval [ADMIN only]")
    public ResponseEntity<ApiResponse<List<AcademyResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending academies", academyService.getPending()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get academy by ID", description = "No token required.")
    public ResponseEntity<ApiResponse<AcademyResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Academy fetched", academyService.getById(id)));
    }

    @GetMapping("/mine")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List academies owned by the authenticated user")
    public ResponseEntity<ApiResponse<List<AcademyResponse>>> getMine(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("My academies", academyService.getMyAcademies(user)));
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new academy (wizard submit)",
               description = "Multipart: 'data' = JSON body, 'images' = logo [0] + cover [1].")
    public ResponseEntity<ApiResponse<AcademyResponse>> create(
            @RequestPart("data") @Valid CreateAcademyRequest data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Academy created", academyService.create(data, images, user)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update academy profile",
               description = "Multipart: 'data' = JSON body, 'images' = logo [0] + cover [1] (optional, replaces existing).")
    public ResponseEntity<ApiResponse<AcademyResponse>> update(
            @PathVariable Long id,
            @RequestPart("data") UpdateAcademyRequest data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Academy updated", academyService.update(id, data, images, user)));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete an academy")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        academyService.delete(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Academy deleted", null));
    }

    // ── Approval ──────────────────────────────────────────────────────────────

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Approve an academy [ADMIN only]")
    public ResponseEntity<ApiResponse<AcademyResponse>> approve(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Academy approved", academyService.approve(id)));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Reject an academy [ADMIN only]")
    public ResponseEntity<ApiResponse<AcademyResponse>> reject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Academy rejected", academyService.reject(id)));
    }

    // ── Branch sub-resource ───────────────────────────────────────────────────

    @PostMapping(value = "/{id}/branches", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a branch to an academy",
               description = "Multipart: 'data' = JSON body, 'images' = branch photos.")
    public ResponseEntity<ApiResponse<AcademyResponse>> addBranch(
            @PathVariable Long id,
            @RequestPart("data") @Valid AcademyBranchRequest data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Branch added", academyService.addBranch(id, data, images, user)));
    }

    @PutMapping(value = "/{id}/branches/{branchId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a branch")
    public ResponseEntity<ApiResponse<AcademyResponse>> updateBranch(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @RequestPart("data") AcademyBranchRequest data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Branch updated", academyService.updateBranch(id, branchId, data, images, user)));
    }

    @DeleteMapping("/{id}/branches/{branchId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a branch")
    public ResponseEntity<ApiResponse<AcademyResponse>> removeBranch(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Branch removed", academyService.removeBranch(id, branchId, user)));
    }

    // ── Staff sub-resource ────────────────────────────────────────────────────

    @PostMapping(value = "/{id}/staff", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a staff member",
               description = "Multipart: 'data' = JSON body, 'photo' = staff photo.")
    public ResponseEntity<ApiResponse<AcademyResponse>> addStaff(
            @PathVariable Long id,
            @RequestPart("data") @Valid AcademyStaffRequest data,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Staff added", academyService.addStaff(id, data, photo, user)));
    }

    @PutMapping(value = "/{id}/staff/{staffId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a staff member")
    public ResponseEntity<ApiResponse<AcademyResponse>> updateStaff(
            @PathVariable Long id,
            @PathVariable Long staffId,
            @RequestPart("data") AcademyStaffRequest data,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Staff updated", academyService.updateStaff(id, staffId, data, photo, user)));
    }

    @DeleteMapping("/{id}/staff/{staffId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a staff member")
    public ResponseEntity<ApiResponse<AcademyResponse>> removeStaff(
            @PathVariable Long id,
            @PathVariable Long staffId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Staff removed", academyService.removeStaff(id, staffId, user)));
    }

    // ── Batch sub-resource ────────────────────────────────────────────────────

    @PostMapping("/{id}/batches")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a batch to an academy")
    public ResponseEntity<ApiResponse<AcademyResponse>> addBatch(
            @PathVariable Long id,
            @RequestBody @Valid AcademyBatchRequest data,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Batch added", academyService.addBatch(id, data, user)));
    }

    @PutMapping("/{id}/batches/{batchId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a batch")
    public ResponseEntity<ApiResponse<AcademyResponse>> updateBatch(
            @PathVariable Long id,
            @PathVariable Long batchId,
            @RequestBody AcademyBatchRequest data,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Batch updated", academyService.updateBatch(id, batchId, data, user)));
    }

    @DeleteMapping("/{id}/batches/{batchId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a batch")
    public ResponseEntity<ApiResponse<AcademyResponse>> removeBatch(
            @PathVariable Long id,
            @PathVariable Long batchId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Batch removed", academyService.removeBatch(id, batchId, user)));
    }
}
