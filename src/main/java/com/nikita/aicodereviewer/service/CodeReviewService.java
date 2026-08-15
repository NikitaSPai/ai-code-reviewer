package com.nikita.aicodereviewer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikita.aicodereviewer.dto.CodeReviewRequest;
import com.nikita.aicodereviewer.dto.CodeReviewResponse;
import com.nikita.aicodereviewer.dto.gemini.Content;
import com.nikita.aicodereviewer.dto.gemini.GeminiRequest;
import com.nikita.aicodereviewer.dto.gemini.Part;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CodeReviewService {

  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  @Value("${gemini.api.url}")
  private String apiUrl;

  @Value("${gemini.api.key}")
  private String apiKey;

  public CodeReviewResponse review(CodeReviewRequest request) {

    GeminiRequest geminiRequest = buildGeminiRequest(request);

    String response =
        restClient
            .post()
            .uri(apiUrl)
            .header("x-goog-api-key", apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(geminiRequest)
            .retrieve()
            .body(String.class);

    try {
      JsonNode root = objectMapper.readTree(response);

      String answer =
          root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

      return objectMapper.readValue(answer, CodeReviewResponse.class);

    } catch (Exception e) {
      throw new RuntimeException("Unable to parse Gemini response", e);
    }
  }

  private static GeminiRequest buildGeminiRequest(CodeReviewRequest request) {

    String prompt =
        """
    You are an expert Java backend code reviewer.

    Review this code for:
    - bugs
    - performance issues
    - Spring Boot issues
    - exception handling
    - security issues
    - Sonar issues
    - DRY violations

    Return ONLY valid JSON:
    {
      "summary": "Short summary",
      "issues": [
        {
          "severity": "HIGH",
          "issue": "Problem",
          "suggestion": "Fix"
        }
      ],
      "improvedCode": "Improved code"
    }

    Code:
    """
            + request.getCode();

    return new GeminiRequest(List.of(new Content(List.of(new Part(prompt)))));
  }
}
