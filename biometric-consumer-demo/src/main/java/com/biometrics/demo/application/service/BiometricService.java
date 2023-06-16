package com.biometrics.demo.application.service;

import com.biometrics.demo.application.model.BiometricValidationResponse;
import com.biometrics.demo.application.model.GenericResponse;
import com.biometrics.demo.application.model.ImageRequest;
import java.io.IOException;
import reactor.core.publisher.Mono;

public interface BiometricService {

  Mono<GenericResponse<BiometricValidationResponse>> validate(ImageRequest data) throws IOException;
}
