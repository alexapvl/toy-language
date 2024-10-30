package model.adt.stack.exceptions;

import model.exceptions.AppException;

public class StackEmptyAppExcetion extends AppException {
    public StackEmptyAppExcetion(String message) {
        super(message);
    }
}
