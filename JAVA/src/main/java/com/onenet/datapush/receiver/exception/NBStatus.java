package com.onenet.datapush.receiver.exception;

public enum NBStatus {
    HTTP_REQUEST_ERROR(1, "http request error"),
    LOAD_CONFIG_ERROR(2, "load config error")
    ;
    private String error;
    private int errorNo;
    NBStatus(int errorNo, String error) {
        this.error = error;
        this.errorNo = errorNo;
    }
    public String getError() {
        return error;
    }

    public int getErrorNo() {
        return errorNo;
    }
}
