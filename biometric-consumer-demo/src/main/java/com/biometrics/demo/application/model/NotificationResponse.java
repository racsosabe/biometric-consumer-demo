package com.biometrics.demo.application.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {

  private String category;
  private ErrorCode code;

  private String message;

  private String description;

  private String action;

  private String uuid;

  private String timestamp;

  private String severity;

  public NotificationResponse() {
    this.timestamp = LocalDateTime.now().toString();
  }
}
