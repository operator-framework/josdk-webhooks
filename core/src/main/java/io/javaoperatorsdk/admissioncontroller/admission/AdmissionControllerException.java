package io.javaoperatorsdk.admissioncontroller.admission;

public class AdmissionControllerException extends RuntimeException {

  public AdmissionControllerException() {}

  public AdmissionControllerException(String message) {
    super(message);
  }

  public AdmissionControllerException(String message, Throwable cause) {
    super(message, cause);
  }

  public AdmissionControllerException(Throwable cause) {
    super(cause);
  }

  public AdmissionControllerException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
