package com.wavemark.scheduler.schedule.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class ValidationExceptionHandlerTest {

    @Mock
    BindingResult bindingResult;

    @Mock
    MethodArgumentNotValidException methodArgumentNotValidException;

    @Test
    void testProcessException() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        ValidationExceptionHandler validationExceptionHandler = new ValidationExceptionHandler();

        ResponseEntity<List<String>> result = validationExceptionHandler
                .processException(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

}