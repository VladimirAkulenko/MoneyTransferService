package ru.netology.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.logger.Logger;
import ru.netology.logger.LoggerImpl;
import ru.netology.exception.ErrorTransferOrConfirmException;
import ru.netology.exception.InvalidDataException;

import java.util.concurrent.atomic.AtomicInteger;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger logger;
    private static final AtomicInteger errorId = new AtomicInteger(1);

    public ExceptionHandlerAdvice() {
        logger = LoggerImpl.getInstance();
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsHandler(InvalidDataException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(errorId.getAndIncrement(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransferOrConfirmException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsHandler(ErrorTransferOrConfirmException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(errorId.getAndIncrement(), e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
