package com.nikita.aicodereviewer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nikita.aicodereviewer.dto.CodeReviewResponse;
import com.nikita.aicodereviewer.service.CodeReviewService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CodeReviewController.class)
class CodeReviewControllerTest {
  @Autowired MockMvc mockMvc;
  @MockitoBean CodeReviewService codeReviewService;

  @Test
  void shouldReturnBadRequestForEmptyCode() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/code-review").contentType("application/json").content("{\"code\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnOkForValidCode() throws Exception {
    when(codeReviewService.review(any()))
        .thenReturn(new CodeReviewResponse("Looks good", List.of(), ""));
    mockMvc
        .perform(
            post("/api/v1/code-review")
                .contentType("application/json")
                .content("{\"code\":\"public class UserService { void test() {} }\"}"))
        .andExpect(status().isOk());
  }
}
