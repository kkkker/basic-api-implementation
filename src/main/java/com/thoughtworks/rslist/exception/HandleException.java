package com.thoughtworks.rslist.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler({EventRangeException.class,
            EventIndexException.class})
    public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        if (ex instanceof EventRangeException) {
            exceptionMessage.setError("invalid request param");
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        if (ex instanceof EventIndexException) {
            exceptionMessage.setError("invalid index");
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        return ResponseEntity.status(400).body(null);
    }
}
