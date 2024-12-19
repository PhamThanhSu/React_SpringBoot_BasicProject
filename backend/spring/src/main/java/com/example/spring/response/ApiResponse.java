package com.example.spring.response;

public class ApiResponse<T> {
    private int status;
    private String message;
    private int EC;
    private T data;

    public ApiResponse(int status, String message, int EC, T data) {
        this.status = status;
        this.message = message;
        this.EC = EC;
        this.data = data;
    }

    public ApiResponse(int EC, String message, int status) {
        this.EC = EC;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(int EC, String message) {
        this.EC = EC;
        this.message = message;
    }

    // Getters và Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getEC() {
        return EC;
    }

    public void setEC(int EC) {
        this.EC = EC;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
