package com.biometrics.demo.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObservationItem {

  @JsonProperty("valor")
  private String value;

  @JsonProperty("resultado")
  private String result;

  @JsonProperty("idParametroValidacion")
  private String validationParameterId;

  @JsonProperty("valorMinimo")
  private String minimumValue;

  @JsonProperty("valorMaximo")
  private String maximumValue;

  @JsonProperty("idParametro")
  private String parameterId;

  @JsonProperty("orden")
  private int order;

  @JsonProperty("tipoCaracteristica")
  private int characteristicType;

  @JsonProperty("nombre")
  private String name;

  @JsonProperty("unidad")
  private String unit;

  @JsonProperty("tipoValor")
  private int valueType;
}
