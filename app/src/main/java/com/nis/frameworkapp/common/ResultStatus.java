package com.nis.frameworkapp.common;

public class ResultStatus<T> {
    public ProgressStatus progressStatus = ProgressStatus.NOT_STARTED;
    public ErrorResponse error = new ErrorResponse();
    public T result;

    public enum ProgressStatus {
        NOT_STARTED,
        STARTED,
        SUCCESS,
        FAILURE
    }

    public class ErrorResponse {
        public String errorMessage;
        public int errorCode;
    }
}
