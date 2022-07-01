package io.javaoperatorsdk.webhook.admission;

import io.fabric8.kubernetes.api.model.Status;

public class NotAllowedException extends AdmissionControllerException {

  private final Status status;

  public NotAllowedException() {
    status = new Status();
    status.setCode(403);
  }

  public NotAllowedException(Status status) {
    this.status = status;
  }

  public NotAllowedException(Throwable cause, Status status) {
    super(cause);
    this.status = status;
  }

  public NotAllowedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, Status status) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.status = status;
  }

  public NotAllowedException(String message) {
    super(message);
    this.status = new Status();
    this.status.setMessage(message);
    this.status.setCode(403);
  }

  public NotAllowedException(int code) {
    this.status = new Status();
    this.status.setCode(code);
  }

  public NotAllowedException(String message, int code) {
    super(message);
    this.status = new Status();
    this.status.setCode(code);
    this.status.setMessage(message);
  }

  public NotAllowedException(String message, Throwable cause, int code) {
    super(message, cause);
    this.status = new Status();
    this.status.setCode(code);
    this.status.setMessage(message);
  }

  public NotAllowedException(Throwable cause, int code) {
    super(cause);
    this.status = new Status();
    this.status.setCode(code);
  }

  public NotAllowedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, int code) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.status = new Status();
    status.setCode(code);
  }

  public Status getStatus() {
    return status;
  }
}
