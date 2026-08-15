package com.nikita.aicodereviewer.controller;

import com.nikita.aicodereviewer.dto.CodeReviewRequest;
import com.nikita.aicodereviewer.dto.CodeReviewResponse;
import com.nikita.aicodereviewer.service.CodeReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/code-review")
@RequiredArgsConstructor
@Tag(name = "AI Code Review API", description = "Review Java code using Google Gemini AI")
public class CodeReviewController {
  private final CodeReviewService codeReviewService;

  @Operation(summary = "Review Java code using AI")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Code review completed"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "429", description = "Gemini API quota exceeded")
  })
  @PostMapping
  public ResponseEntity<CodeReviewResponse> review(@Valid @RequestBody CodeReviewRequest request) {
    return ResponseEntity.ok(codeReviewService.review(request));
  }
}
