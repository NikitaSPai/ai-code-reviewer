package com.nikita.aicodereviewer.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeReviewResponse {
  private String summary;
  private List<ReviewIssue> issues;
  private String improvedCode;
}
