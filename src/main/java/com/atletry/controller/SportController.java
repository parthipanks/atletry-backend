package com.atletry.controller;

import com.atletry.dto.request.CreateSportRequest;
import com.atletry.dto.request.UpdateSportRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.SportResponse;
import com.atletry.service.SportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/sports")
@RequiredArgsConstructor
@Tag(name = "2. Sports Catalogue", description = "Dynamic sport management — list, create, update, soft-delete")
public class SportController {

    private final SportService sportService;


    @GetMapping
    @Operation(
        summary = "List all active sports ",
        description = "Returns all active sports ordered by `displayOrder`. "
                    + "Each sport includes its 3 skill level options. **No token required.**"
    )
    public ResponseEntity<ApiResponse<List<SportResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Sports fetched", sportService.getAllActive()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sport by ID", description = "No token required.")
    public ResponseEntity<ApiResponse<SportResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Sport fetched", sportService.getById(id)));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Create sport ",
        description = "Adds a new sport. Three skill levels are **auto-generated** "
                    + "(Just Starting / Intermediate / Competitive). "
                    + "Upload an icon via the `icon` file part (JPEG / PNG / WebP / SVG, max 5 MB). "
                    + "Requires JWT."
    )

    public ResponseEntity<ApiResponse<SportResponse>> create(
            @ParameterObject @Valid @ModelAttribute CreateSportRequest req,
            @Parameter(description = "Sport icon image (JPEG / PNG / WebP / SVG, max 5 MB)",
                       content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                          schema = @Schema(type = "string", format = "binary")))
            @RequestParam(value = "icon", required = false) MultipartFile icon) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Sport created", sportService.create(req, icon)));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update sport  ", description = "Partial update — only provided fields change.")
    public ResponseEntity<ApiResponse<SportResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSportRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Sport updated", sportService.update(id, req)));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Delete sport  🔐",
        description = "Soft-deletes the sport (`isActive = false`). "
                    + "Existing user selections are preserved."
    )
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        sportService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Sport deactivated"));
    }
}
