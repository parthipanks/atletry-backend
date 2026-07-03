package com.atletry.controller;

import com.atletry.dto.request.CreateTournamentRequest;
import com.atletry.dto.request.RegisterTournamentRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.TournamentResponse;
import com.atletry.entity.User;
import com.atletry.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TournamentResponse>>> getTournaments(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) Long sportId) {
        List<TournamentResponse> tournaments = tournamentService.getTournaments(currentUser, sportId);
        return ResponseEntity.ok(ApiResponse.ok("Tournaments fetched", tournaments));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List tournaments awaiting approval  [ADMIN only]")
    public ResponseEntity<ApiResponse<List<TournamentResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending tournaments", tournamentService.getPendingApproval()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> getById(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        TournamentResponse tournament = tournamentService.getById(id, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Tournament fetched", tournament));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a tournament",
               description = "Send `data` part as JSON and optional `images` parts for tournament photos (multiple files allowed).")
    public ResponseEntity<ApiResponse<TournamentResponse>> create(
            @AuthenticationPrincipal User currentUser,
            @RequestPart("data") @Valid CreateTournamentRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        TournamentResponse tournament = tournamentService.create(currentUser, req, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tournament created", tournament));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<ApiResponse<TournamentResponse>> register(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestBody RegisterTournamentRequest req) {
        TournamentResponse tournament = tournamentService.register(currentUser, id, req);
        return ResponseEntity.ok(ApiResponse.ok("Registered for tournament", tournament));
    }

    @DeleteMapping("/{id}/register")
    public ResponseEntity<ApiResponse<Void>> cancelRegistration(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {
        tournamentService.cancelRegistration(currentUser, id);
        return ResponseEntity.ok(ApiResponse.ok("Registration cancelled"));
    }
}
