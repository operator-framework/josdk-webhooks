package io.javaoperatorsdk.webhook.conversion;

public class MissingConversionMapperException extends ConversionException {

  public MissingConversionMapperException() {}

  public MissingConversionMapperException(String message) {
    super(message);
  }
}
