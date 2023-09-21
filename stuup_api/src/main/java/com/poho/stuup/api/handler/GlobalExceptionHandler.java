package com.poho.stuup.api.handler;

import cn.dev33.satoken.exception.NotSafeException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.util.StrUtil;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.saToken.SaTokenExceptionCodeEnum;
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


    @ExceptionHandler(SaTokenException.class)
    @ResponseBody
    public ResponseModel handlerSaTokenException(SaTokenException e) {
        String errorMsg = SaTokenExceptionCodeEnum.getEnumMsgByCode(e.getCode());
        ResponseModel responseModel = ResponseModel.failed(e.getMessage());
        responseModel.setCode(e.getCode());

        if (StrUtil.isNotBlank(errorMsg)) {
            responseModel.setMsg(errorMsg);
        }
        if (e instanceof NotSafeException) {
            responseModel.setData(((NotSafeException) e).getService());
        }

        return responseModel;

    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseModel exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseModel.failed(e.getMessage());
    }
}
