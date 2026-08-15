package com.nikita.aicodereviewer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Java code review request")
public class CodeReviewRequest {
  @Schema(description = "Java code to review")
  @NotBlank(message = "Code cannot be empty")
  @Pattern(
      regexp = "^(?s)(?=.*[A-Za-z]).{10,}$",
      message = "Code must contain at least 10 characters and one letter")
  private String code;
}
