package com.nikita.aicodereviewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/** Configuration class for RestClient bean. */
@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    return RestClient.builder().build();
  }
}
