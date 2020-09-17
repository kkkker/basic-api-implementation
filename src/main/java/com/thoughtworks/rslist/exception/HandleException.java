package com.thoughtworks.rslist.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler({EventRangeException.class,
            EventIndexException.class})
    public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        Logger logger = LoggerFactory.getLogger(HandleException.class);

        if (ex instanceof EventRangeException) {
            exceptionMessage.setError("invalid request param");
            logger.error(exceptionMessage.getError());
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        if (ex instanceof EventIndexException) {
            exceptionMessage.setError("invalid index");
            logger.error(exceptionMessage.getError());
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        logger.error(exceptionMessage.getError());
        return ResponseEntity.status(400).body(null);
    }
}
