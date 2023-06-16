package com.biometrics.demo.application.service;

import com.biometrics.demo.application.configuration.SuneduEndpointProperties;
import com.biometrics.demo.application.constants.StringConstants;
import com.biometrics.demo.application.exception.AttachErrorException;
import com.biometrics.demo.application.exception.ListErrorException;
import com.biometrics.demo.application.model.AttachResponse;
import com.biometrics.demo.application.model.BiometricValidationResponse;
import com.biometrics.demo.application.model.GenericResponse;
import com.biometrics.demo.application.model.ImageRequest;
import com.biometrics.demo.application.util.DataProcessingUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BiometricServiceImpl implements BiometricService {

  @Autowired private WebClient webClient;

  @Autowired private SuneduEndpointProperties suneduEndpointProperties;

  @Override
  public Mono<GenericResponse<BiometricValidationResponse>> validate(ImageRequest data)
      throws IOException {
    return sendImage(data)
        .flatMap(
            attachResponse -> {
              if (!attachResponse.getData().containsKey(StringConstants.ID)) {
                return Mono.error(
                    new AttachErrorException("Attach response doesn't contain document id"));
              }
              String id = attachResponse.getData().get(StringConstants.ID).toString();
              return getObservations(id);
            })
        .onErrorResume(
            error -> {
              throw new AttachErrorException(
                  StringConstants.BASIC_ERROR_MESSAGE + error.getMessage());
            })
        .map(
            list ->
                list.stream()
                    .filter(observation -> observation.charAt(0) == '0')
                    .map(observation -> observation.substring(2))
                    .collect(Collectors.toList()))
        .map(
            finalList ->
                new BiometricValidationResponse(
                    finalList.isEmpty() ? StringConstants.VALID : StringConstants.INVALID,
                    LocalDateTime.now().toString(),
                    finalList))
        .map(GenericResponse::new);
  }

  private Mono<AttachResponse> sendImage(ImageRequest data) throws IOException {
    String uuid = UUID.randomUUID().toString();
    FileUtils.writeByteArrayToFile(
        new File(
            uuid + "." + data.getContentType().substring(data.getContentType().indexOf('/') + 1)),
        Base64Utils.decodeFromString(data.getContent()));
    File imageFile =
        new File(
            uuid + "." + data.getContentType().substring(data.getContentType().indexOf('/') + 1));
    return webClient
        .post()
        .uri(suneduEndpointProperties.getAttach())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromMultipartData(DataProcessingUtils.buildImageForm(imageFile)))
        .exchangeToMono(
            response -> {
              if (imageFile.delete()) {
                log.info("File " + uuid + " deleted successfully");
              } else {
                log.info("File " + uuid + " wasn't deleted");
              }
              if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(AttachResponse.class);
              } else if (response.statusCode().is4xxClientError()) {
                return response
                    .bodyToMono(String.class)
                    .flatMap(
                        body ->
                            Mono.error(
                                new Exception(
                                    MessageFormat.format(
                                        StringConstants.FORMATTED_ERROR_MESSAGE,
                                        response.statusCode().toString(),
                                        suneduEndpointProperties.getAttach()))));
              } else {
                return response.createException().flatMap(Mono::error);
              }
            })
        .onErrorResume(
            error -> {
              if (imageFile.delete()) {
                log.info("File " + uuid + " deleted successfully");
              } else {
                log.info("File " + uuid + " wasn't deleted");
              }
              throw new AttachErrorException(
                  StringConstants.BASIC_ERROR_MESSAGE + error.getMessage());
            });
  }

  private Mono<List<String>> getObservations(String id) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(suneduEndpointProperties.getList())
                    .queryParam(StringConstants.ID, id)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(
            response -> {
              if (response.statusCode().is2xxSuccessful()) {
                return response
                    .bodyToMono(String.class)
                    .map(
                        data -> {
                          try {
                            return DataProcessingUtils.convertToObservationItemList(data);
                          } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                          }
                        })
                    .map(DataProcessingUtils::buildObservationsList);
              } else if (response.statusCode().is4xxClientError()) {
                return response
                    .bodyToMono(String.class)
                    .flatMap(
                        body ->
                            Mono.error(
                                new Exception(
                                    MessageFormat.format(
                                        StringConstants.FORMATTED_ERROR_MESSAGE,
                                        response.statusCode().toString(),
                                        suneduEndpointProperties.getList()))));
              } else {
                return response.createException().flatMap(Mono::error);
              }
            })
        .onErrorResume(
            error -> {
              throw new ListErrorException(
                  StringConstants.BASIC_ERROR_MESSAGE + error.getMessage());
            });
  }
}
