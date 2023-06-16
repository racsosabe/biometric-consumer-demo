package com.biometrics.demo.application.util;

import com.biometrics.demo.application.model.ObservationItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;

public class DataProcessingUtils {

  public static MultiValueMap<String, HttpEntity<?>> buildImageForm(File imageFile) {
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    builder.part("file", new FileSystemResource(imageFile));
    return builder.build();
  }

  public static List<String> buildObservationsList(List<ObservationItem> data) {
    return data.stream()
        .map(ObservationItem::getResult)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public static List<ObservationItem> convertToObservationItemList(String data)
      throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(data, new TypeReference<>() {});
  }
}
