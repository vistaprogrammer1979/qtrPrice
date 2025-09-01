package com.santechture.entity;

public class GenericGeneralResponse<T> {
    private int statusCode;
    private String statusMessage;
    private T data;

    public GenericGeneralResponse(int statusCode, String statusMessage, T data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public T getData() {
        return this.data;
    }
}
