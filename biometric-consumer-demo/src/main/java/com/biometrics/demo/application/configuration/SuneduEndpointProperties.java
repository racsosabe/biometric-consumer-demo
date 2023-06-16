package com.biometrics.demo.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "sunedu.api")
public class SuneduEndpointProperties {

  private String attach;
  private String list;
}
