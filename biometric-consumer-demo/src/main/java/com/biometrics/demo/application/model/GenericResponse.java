package com.biometrics.demo.application.model;

import java.util.ArrayList;
import java.util.List;

public class GenericResponse<T> {

  T data;

  List<NotificationResponse> notifications;

  public GenericResponse(T data) {
    this.data = data;
    this.notifications = new ArrayList<>();
  }

  public GenericResponse(List<NotificationResponse> notificationResponseList) {
    this.notifications = new ArrayList<>(notificationResponseList);
  }

  public GenericResponse(T data, List<NotificationResponse> notificationResponseList) {
    this.data = data;
    this.notifications = new ArrayList<>(notificationResponseList);
  }

  public T getData() {
    return data;
  }

  public List<NotificationResponse> getNotifications() {
    return new ArrayList<>(notifications);
  }
}
