package model.adt.dictionary.exceptions;

import model.exceptions.AppException;

public class KeyNotFoundAppException extends AppException {
  public KeyNotFoundAppException(String message) {
    super(message);
  }
}