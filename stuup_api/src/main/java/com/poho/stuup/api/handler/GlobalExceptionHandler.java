package com.poho.stuup.api.handler;

import com.poho.common.custom.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseModel methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                System.out.println("field" + error.getField() + ", msg:" + error.getDefaultMessage());
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
        }

        log.warn(errorMsg.toString(), e);
        return ResponseModel.failed(errorMsg.toString());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ResponseModel constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.warn(e.getMessage(), e);
        return ResponseModel.failed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseModel exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseModel.failed(e.getMessage());
    }
}
