package com.wangkang.goodwillghservice.share.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    private final Map<String, Object> messageMap;

    public ResponseBuilder() {
        messageMap = new HashMap<>();
    }

    public ResponseBuilder put(String key, Object value) {
        messageMap.put(key, value);
        return this;
    }

    public ResponseBuilder message(String message) {
        return put("message", message);
    }

    public static ResponseEntity<Object> badRequest(String message) {
        Map<String, Object> build = new ResponseBuilder().message(message).build();
        return ResponseEntity.badRequest().body(build);
    }

    public static ResponseEntity<Object> ok(String message) {
        Map<String, Object> build = new ResponseBuilder().message(message).build();
        return ResponseEntity.ok().body(build);
    }
    public static ResponseEntity<Object> ok(Object data) {
        return ResponseEntity.ok().body(data);
    }

    public static ResponseEntity<Object> notFound(String message) {
        Map<String, Object> build = new ResponseBuilder().message(message).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(build);
    }

    public static ResponseStatusException notFoundException(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, GsonUtils.toJson(ResponseBuilder.msg(message)));
    }

    public static ResponseBuilder msg(String message) {
        return new ResponseBuilder().message(message);
    }

    public Map<String, Object> build() {
        return messageMap;
    }

    public static Map<String, Object> buildUploadSuccessMessage() {
        return new ResponseBuilder().message("上传成功").build();
    }

    public static Map<String, Object> buildUploadNullFileMessage() {
        return new ResponseBuilder().message("上传失败，文件为空").build();
    }

    public static Map<String, Object> buildUploadFailedMessage() {
        return new ResponseBuilder().message("上传失败").build();
    }
}
