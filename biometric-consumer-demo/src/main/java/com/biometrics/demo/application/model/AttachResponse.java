package com.biometrics.demo.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachResponse {
  private Map<String, Object> data;
  private boolean success;
  private boolean hasErrors;
  private boolean hasWarnings;
  private List<String> messages;

  public AttachResponse(
      Map<String, Object> data,
      boolean success,
      boolean hasErrors,
      boolean hasWarnings,
      List<String> messages) {
    this.data = new HashMap<>(data);
    this.success = success;
    this.hasErrors = hasErrors;
    this.hasWarnings = hasWarnings;
    this.messages = new ArrayList<>(messages);
  }

  public Map<String, Object> getData() {
    return new HashMap<>(data);
  }

  public List<String> getMessages() {
    return new ArrayList<>(messages);
  }
}
