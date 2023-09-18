package com.biometrics.demo.adapter.web;

import com.biometrics.demo.application.model.BiometricValidationResponse;
import com.biometrics.demo.application.model.GenericResponse;
import com.biometrics.demo.application.model.ImageRequest;
import com.biometrics.demo.application.service.BiometricService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/biometric")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BiometricController {

  @Autowired private BiometricService biometricService;

  @PostMapping(value = "/validate")
  @ResponseStatus(HttpStatus.OK)
  public Mono<GenericResponse<BiometricValidationResponse>> validateImage(
      @RequestBody ImageRequest data) throws IOException {
    return biometricService.validate(data);
  }
}
