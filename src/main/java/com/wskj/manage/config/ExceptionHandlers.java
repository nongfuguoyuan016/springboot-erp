package com.wskj.manage.config;

import com.wskj.manage.common.utils.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value = RuntimeException.class)
    public JSONResult catchException(Exception ex) {
        log.error("exception hadnler: ",ex);
        return JSONResult.ex("系统异常,请稍后再试");
    }
}
