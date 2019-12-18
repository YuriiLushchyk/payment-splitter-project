package com.eleks.userservice.advisor;

import com.eleks.userservice.dto.ErrorDto;
import com.eleks.userservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto handleNotFoundException(Exception exception) {
        String msg = exception.getMessage() == null ? "Resource not found" : exception.getMessage();
        return ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

