package com.biometrics.demo.application.exception;

import com.biometrics.demo.application.model.ErrorCode;
import com.biometrics.demo.application.model.GenericResponse;
import com.biometrics.demo.application.model.NotificationResponse;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AttachErrorException.class)
  public final ResponseEntity<GenericResponse<Object>> handleAttachErrorException(Exception ex) {
    String uuid = UUID.randomUUID().toString();
    NotificationResponse notification = new NotificationResponse();
    notification.setCategory(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    notification.setCode(ErrorCode.BD001);
    notification.setMessage("Something failed during attach request");
    notification.setDescription("Attach Error Exception thrown");
    notification.setAction("Report to development team in charge");
    notification.setUuid(uuid);
    notification.setSeverity("ERROR");
    List<NotificationResponse> notificationList = Collections.singletonList(notification);
    logError(uuid, ex);
    return new ResponseEntity<>(
        new GenericResponse<>(notificationList), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ListErrorException.class)
  public final ResponseEntity<GenericResponse<Object>> handleListErrorException(Exception ex) {
    String uuid = UUID.randomUUID().toString();
    NotificationResponse notification = new NotificationResponse();
    notification.setCategory(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    notification.setCode(ErrorCode.BD002);
    notification.setMessage("Something failed during list request");
    notification.setDescription("List Error Exception thrown");
    notification.setAction("Report to development team in charge");
    notification.setUuid(uuid);
    notification.setSeverity("ERROR");
    List<NotificationResponse> notificationList = Collections.singletonList(notification);
    logError(uuid, ex);
    return new ResponseEntity<>(
        new GenericResponse<>(notificationList), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<GenericResponse<Object>> handleRuntimeException(Exception ex) {
    String uuid = UUID.randomUUID().toString();
    NotificationResponse notification = new NotificationResponse();
    notification.setCategory(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    notification.setCode(ErrorCode.BD000);
    notification.setMessage("Something failed during execution, check logs");
    notification.setDescription("List Error Exception thrown");
    notification.setAction("Report to development team in charge");
    notification.setUuid(uuid);
    notification.setSeverity("ERROR");
    List<NotificationResponse> notificationList = Collections.singletonList(notification);
    logError(uuid, ex);
    return new ResponseEntity<>(
        new GenericResponse<>(notificationList), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  protected void logError(String uuid, Exception ex) {
    log.error(MessageFormat.format("Client error with response reference: {0}", uuid));
    log.error("Exception traceback: ", ex);
  }
}
