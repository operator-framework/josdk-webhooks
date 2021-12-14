package io.javaoperatorsdk.admissioncontroller;

import io.fabric8.kubernetes.api.model.Status;

public class NotAllowedException extends AdmissionControllerException {

  private Status status = new Status();

  public NotAllowedException() {
    status.setCode(403);
  }

  public NotAllowedException(Status status) {
    this.status = status;
  }

  public NotAllowedException(Throwable cause, Status status) {
    super(cause);
    this.status = status;
  }

  public NotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Status status) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.status = status;
  }

  public NotAllowedException(String message) {
    super(message);
    this.status.setCode(403);
  }

  public NotAllowedException(int code) {
    this.status.setCode(code);
  }

  public NotAllowedException(String message, int code) {
    super(message);
    this.status.setCode(code);
    this.status.setMessage(message);
  }

  public NotAllowedException(String message, Throwable cause, int code) {
    super(message, cause);
    this.status.setCode(code);
    this.status.setMessage(message);
  }

  public NotAllowedException(Throwable cause, int code) {
    super(cause);
    this.status.setCode(code);
  }

  public NotAllowedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, int code) {
    super(message, cause, enableSuppression, writableStackTrace);
    status.setCode(code);
  }

  public Status getStatus() {
    return status;
  }
}
