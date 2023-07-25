package com.clonecoding.pinterest.global.exception.validation;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
@Slf4j(topic = "ValidataionUtil")
public class ValidationUtil {
    public void bindingResultHandle(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                throw new ValidationException(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }
    }
}
