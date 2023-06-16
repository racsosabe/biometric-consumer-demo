package com.biometrics.demo.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BiometricValidationResponse {

  private final String validationVeredict;
  private final String date;
  private final List<String> observations;

  public BiometricValidationResponse(
      String validationVeredict, String date, List<String> observations) {
    this.validationVeredict = validationVeredict;
    this.date = date;
    this.observations = new ArrayList<>(observations);
  }

  public List<String> getObservations() {
    return new ArrayList<>(observations);
  }

  public String getValidationVeredict() {
    return validationVeredict;
  }

  public String getDate() {
    return date;
  }
}
