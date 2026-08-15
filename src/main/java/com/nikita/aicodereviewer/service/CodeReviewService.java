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
            You are an expert Java backend developer and code reviewer.

            Review the following Java code. Check for bugs, N+1 queries,
            inefficient code, poor exception handling, null risks, Sonar issues,
            DRY violations, incorrect Spring Boot usage, security issues,
            performance issues, and readability.

            Return ONLY valid JSON in this format:

            {
              "summary": "Short summary",
              "issues": [
                {
                  "severity": "HIGH",
                  "issue": "Problem found",
                  "suggestion": "How to fix it"
                }
              ],
              "improvedCode": "Improved Java code"
            }

            Rules:
            - Use HIGH, MEDIUM, or LOW for severity.
            - Use an empty issues array when no issues are found.
            - Include improved Java code when useful.
            - Do not return markdown or wrap the response in code fences.

            Code to review:
            """
            + request.getCode();

    return new GeminiRequest(List.of(new Content(List.of(new Part(prompt)))));
  }
}
