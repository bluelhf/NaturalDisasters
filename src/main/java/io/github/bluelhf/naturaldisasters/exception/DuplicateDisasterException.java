package io.github.bluelhf.naturaldisasters.exception;

public class DuplicateDisasterException extends Exception {
    public DuplicateDisasterException(String errorMessage) { super(errorMessage); }
    public DuplicateDisasterException(String errorMessage, Throwable err) { super(errorMessage, err); }
}
