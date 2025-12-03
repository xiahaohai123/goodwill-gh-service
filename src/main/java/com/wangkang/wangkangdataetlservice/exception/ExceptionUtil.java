package com.wangkang.wangkangdataetlservice.exception;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static Throwable getRootCause(Exception e) {
        Throwable finalThrowble = e;
        while (finalThrowble.getCause() != null) {
            finalThrowble = e.getCause();
        }
        return finalThrowble;
    }
}
