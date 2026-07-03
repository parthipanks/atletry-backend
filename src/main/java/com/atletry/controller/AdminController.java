package com.atletry.controller;

import com.atletry.dto.response.AcademyResponse;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.CoachResponse;
import com.atletry.dto.response.GroundResponse;
import com.atletry.dto.response.MatchResponse;
import com.atletry.dto.response.TournamentResponse;
import com.atletry.service.AcademyService;
import com.atletry.service.CoachService;
import com.atletry.service.GroundService;
import com.atletry.service.MatchService;
import com.atletry.service.TournamentService;
import com.atletry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")   // covers all endpoints; SUPER_ADMIN inherits via role hierarchy
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin — Approvals", description = "ADMIN/SUPER_ADMIN: approve/reject content. SUPER_ADMIN only: manage user roles.")
public class AdminController {

    private final GroundService     groundService;
    private final MatchService      matchService;
    private final TournamentService tournamentService;
    private final CoachService      coachService;
    private final AcademyService    academyService;
    private final UserService       userService;

    // ── Grounds ──────────────────────────────────────────────────────────────

    @GetMapping("/grounds/pending")
    @Operation(summary = "List grounds awaiting approval")
    public ResponseEntity<ApiResponse<List<GroundResponse>>> pendingGrounds() {
        return ResponseEntity.ok(ApiResponse.ok("Pending grounds", groundService.getPendingApproval()));
    }

    @PutMapping("/grounds/{id}/approve")
    @Operation(summary = "Approve a ground")
    public ResponseEntity<ApiResponse<GroundResponse>> approveGround(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Ground approved", groundService.approve(id)));
    }

    @PutMapping("/grounds/{id}/reject")
    @Operation(summary = "Reject a ground")
    public ResponseEntity<ApiResponse<GroundResponse>> rejectGround(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Ground rejected", groundService.reject(id)));
    }

    // ── Matches ───────────────────────────────────────────────────────────────

    @GetMapping("/matches/pending")
    @Operation(summary = "List matches awaiting approval")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> pendingMatches() {
        return ResponseEntity.ok(ApiResponse.ok("Pending matches", matchService.getPendingApproval()));
    }

    @PutMapping("/matches/{id}/approve")
    @Operation(summary = "Approve a match (sets status to OPEN)")
    public ResponseEntity<ApiResponse<MatchResponse>> approveMatch(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Match approved", matchService.approve(id)));
    }

    @PutMapping("/matches/{id}/reject")
    @Operation(summary = "Reject a match (sets status to CANCELLED)")
    public ResponseEntity<ApiResponse<MatchResponse>> rejectMatch(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Match rejected", matchService.reject(id)));
    }

    // ── Tournaments ───────────────────────────────────────────────────────────

    @GetMapping("/tournaments/pending")
    @Operation(summary = "List tournaments awaiting approval")
    public ResponseEntity<ApiResponse<List<TournamentResponse>>> pendingTournaments() {
        return ResponseEntity.ok(ApiResponse.ok("Pending tournaments", tournamentService.getPendingApproval()));
    }

    @PutMapping("/tournaments/{id}/approve")
    @Operation(summary = "Approve a tournament (sets status to OPEN)")
    public ResponseEntity<ApiResponse<TournamentResponse>> approveTournament(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Tournament approved", tournamentService.approve(id)));
    }

    @PutMapping("/tournaments/{id}/reject")
    @Operation(summary = "Reject a tournament (sets status to CANCELLED)")
    public ResponseEntity<ApiResponse<TournamentResponse>> rejectTournament(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Tournament rejected", tournamentService.reject(id)));
    }

    // ── Coaches ───────────────────────────────────────────────────────────────

    @GetMapping("/coaches/pending")
    @Operation(summary = "List coaches awaiting approval")
    public ResponseEntity<ApiResponse<List<CoachResponse>>> pendingCoaches() {
        return ResponseEntity.ok(ApiResponse.ok("Pending coaches", coachService.getPendingApproval()));
    }

    @PutMapping("/coaches/{id}/approve")
    @Operation(summary = "Approve a coach")
    public ResponseEntity<ApiResponse<CoachResponse>> approveCoach(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Coach approved", coachService.approve(id)));
    }

    @PutMapping("/coaches/{id}/reject")
    @Operation(summary = "Reject a coach")
    public ResponseEntity<ApiResponse<CoachResponse>> rejectCoach(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Coach rejected", coachService.reject(id)));
    }

    // ── Academies ────────────────────────────────────────────────────────────

    @GetMapping("/academies/pending")
    @Operation(summary = "List academies awaiting approval")
    public ResponseEntity<ApiResponse<List<AcademyResponse>>> pendingAcademies() {
        return ResponseEntity.ok(ApiResponse.ok("Pending academies", academyService.getPending()));
    }

    @PutMapping("/academies/{id}/approve")
    @Operation(summary = "Approve an academy")
    public ResponseEntity<ApiResponse<AcademyResponse>> approveAcademy(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Academy approved", academyService.approve(id)));
    }

    @PutMapping("/academies/{id}/reject")
    @Operation(summary = "Reject an academy")
    public ResponseEntity<ApiResponse<AcademyResponse>> rejectAcademy(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Academy rejected", academyService.reject(id)));
    }

    // ── User Role Management (SUPER_ADMIN only) ───────────────────────────────

    @PutMapping("/users/{id}/grant-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Grant ADMIN role to a user  [SUPER_ADMIN only]")
    public ResponseEntity<ApiResponse<Void>> grantAdmin(@PathVariable Long id) {
        userService.grantAdmin(id);
        return ResponseEntity.ok(ApiResponse.ok("Admin role granted"));
    }

    @PutMapping("/users/{id}/revoke-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Revoke ADMIN role from a user  [SUPER_ADMIN only]")
    public ResponseEntity<ApiResponse<Void>> revokeAdmin(@PathVariable Long id) {
        userService.revokeAdmin(id);
        return ResponseEntity.ok(ApiResponse.ok("Admin role revoked"));
    }
}
