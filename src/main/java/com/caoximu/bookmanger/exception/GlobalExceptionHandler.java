package com.caoximu.bookmanger.exception;


import com.caoximu.bookmanger.common.R;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BaseBizException.class})
    public R<Void> handleBaseException(BaseBizException e) {
        String message = e.getMessage();
        log.error("业务处理异常, 异常消息:{}, 异常code:{}, 异常msgCode:{}", new Object[]{message, e.getCode(), e.getMsgCode(), e});
        return R.fail(e.getCode(), message, e.getMsgCode(), e.getMsgParams());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public R<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String msg = String.format("请求参数类型不匹配'%s',发生系统异常，参数[%s]要求类型为：'%s'，但输入值为：'%s'", requestURI, e.getName(), e.getRequiredType().getName(), e.getValue());
        log.error(msg);
        return R.fail(500, msg, String.valueOf(500), (Object[])null);
    }

    @ExceptionHandler({RuntimeException.class})
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return R.fail(500, "系統未知異常", String.valueOf(500), (Object[])null);
    }



    @ExceptionHandler({Exception.class})
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return R.fail(500, "系統未知異常", String.valueOf(500), (Object[])null);
    }



    @ExceptionHandler({IllegalArgumentException.class})
    public R<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return R.fail(ex.getMessage());
    }
}