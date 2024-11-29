package com.github.romanqed.devspark.dto;

public final class Response {
    private String message;
    private String cause;

    public Response(String message) {
        this.message = message;
        this.cause = null;
    }

    public Response(String message, Throwable cause) {
        this.message = message;
        this.cause = extractMessage(cause);
    }

    private static String extractMessage(Throwable throwable) {
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
