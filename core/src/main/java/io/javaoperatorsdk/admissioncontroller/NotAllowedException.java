package io.javaoperatorsdk.admissioncontroller;

public class NotAllowedException extends AdmissionControllerException {

  private int code;

  public NotAllowedException() {}

  public NotAllowedException(int code) {
    this.code = code;
  }

  public NotAllowedException(String message, int code) {
    super(message);
    this.code = code;
  }

  public NotAllowedException(String message, Throwable cause, int code) {
    super(message, cause);
    this.code = code;
  }

  public NotAllowedException(Throwable cause, int code) {
    super(cause);
    this.code = code;
  }

  public NotAllowedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, int code) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
