package com.brenokas.ms.user.configs;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {
    @Bean
    public JacksonJsonMessageConverter messageConverter() {
      JsonMapper objectMapper = new JsonMapper();
      return new JacksonJsonMessageConverter(objectMapper);
    }

}
