package com.paywallet.borrowerservice.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(key -> key.getField(), value -> value.getDefaultMessage()));

        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ GeneralHttpException.class })
    public ResponseEntity<Object> handleConstraintViolation(GeneralHttpException exception, WebRequest request) {
        Map<String, String> validationErrors = exception.getErrors();
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler({ ConstraintViolationException.class })
    // public ResponseEntity<Object>
    // handleConstraintViolation(ConstraintViolationException exception,
    // WebRequest request) {
    // Map<String, String> validationErrors =
    // exception.getConstraintViolations().stream()
    // .collect(Collectors.toMap(key -> key.getPropertyPath().toString(), value ->
    // value.getMessage()));
    // return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, request,
    // validationErrors);
    // }

    private ResponseEntity<Object> getExceptionResponseEntity(final HttpStatus status, WebRequest request,
            Map<String, String> errors) {
        final Map<String, Object> body = new LinkedHashMap<>();
        // final String errorsMessage = CollectionUtils.isNotEmpty(errors) ?
        // errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")):status.getReasonPhrase();
        final String path = request.getDescription(false);

        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("errors", errors);
        body.put("path", path);
        body.put("message", status.getReasonPhrase());
        return new ResponseEntity<>(body, status);
    }
}
