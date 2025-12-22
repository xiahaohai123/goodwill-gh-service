package com.wangkang.goodwillghservice.core.exception;

import com.wangkang.goodwillghservice.share.locale.MessageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.getLog(GlobalExceptionHandler.class);
    private final MessageService messageService;
    private static final String KEY_ERROR = "error";

    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(Throwable ex) {
        log.warn(ex.getMessage(), ex);
        Map<String, Object> errors = new HashMap<>();
        errors.put(KEY_ERROR, ex.getMessage());
        return errors;
    }

    @ExceptionHandler(I18nBusinessException.class)
    public ResponseEntity<Object> handleBusinessException(I18nBusinessException ex) {
        String message = messageService.getMessage(
                ex.getCode(),
                ex.getArgs()
        );

        return ResponseEntity.badRequest().body(Map.of(KEY_ERROR, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.debug(ex.getMessage(), ex);

        // 拼接所有校验错误信息为一个字符串
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage) // 只取 message
                .collect(Collectors.joining("; ")); // 用分号分隔，可换成换行 "\n"

        Map<String, String> errors = new HashMap<>();
        errors.put(KEY_ERROR, errorMessage);
        return errors;
    }
}
