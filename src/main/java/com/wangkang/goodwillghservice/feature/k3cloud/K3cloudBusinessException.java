package com.wangkang.goodwillghservice.feature.k3cloud;

public class K3cloudBusinessException extends RuntimeException {
    public K3cloudBusinessException(String message) {
        super(message);
    }

    public K3cloudBusinessException(Throwable cause) {
        super(cause);
    }

    public K3cloudBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
