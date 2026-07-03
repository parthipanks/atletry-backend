package com.atletry.controller;

import com.atletry.dto.request.CreateMatchRequest;
import com.atletry.dto.request.RateMatchRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.MatchResponse;
import com.atletry.dto.response.MyMatchesResponse;
import com.atletry.entity.User;
import com.atletry.service.MatchService;
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
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getOpenMatches(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) Long sportId) {
        List<MatchResponse> matches = matchService.getOpenMatches(currentUser, sportId);
        return ResponseEntity.ok(ApiResponse.ok("Open matches fetched", matches));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List matches awaiting approval  [ADMIN only]")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.ok("Pending matches", matchService.getPendingApproval()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a match",
               description = "Send `data` part as JSON and optional `images` parts for match photos (multiple files allowed).")
    public ResponseEntity<ApiResponse<MatchResponse>> createMatch(
            @AuthenticationPrincipal User currentUser,
            @RequestPart("data") @Valid CreateMatchRequest req,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        MatchResponse match = matchService.createMatch(currentUser, req, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Match created", match));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<MyMatchesResponse>> getMyMatches(
            @AuthenticationPrincipal User currentUser) {
        MyMatchesResponse myMatches = matchService.getMyMatches(currentUser);
        return ResponseEntity.ok(ApiResponse.ok("My matches fetched", myMatches));
    }

    @PostMapping("/{matchId}/join")
    public ResponseEntity<ApiResponse<MatchResponse>> joinMatch(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long matchId) {
        MatchResponse match = matchService.joinMatch(currentUser, matchId);
        return ResponseEntity.ok(ApiResponse.ok("Joined match", match));
    }

    @DeleteMapping("/{matchId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveMatch(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long matchId) {
        matchService.leaveMatch(currentUser, matchId);
        return ResponseEntity.ok(ApiResponse.ok("Left match"));
    }

    @PostMapping("/{matchId}/rate")
    public ResponseEntity<ApiResponse<Void>> rateMatch(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long matchId,
            @Valid @RequestBody RateMatchRequest req) {
        matchService.rateMatch(currentUser, matchId, req);
        return ResponseEntity.ok(ApiResponse.ok("Match rated"));
    }
}
