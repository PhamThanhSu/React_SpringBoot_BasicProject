package com.exception;

public class APIException extends RuntimeException {
    private int status;
    private String message;
    private int EC;
    private Object data; // Đổi thành Object để chứa bất kỳ kiểu dữ liệu nào

    // Constructor mặc định
    public APIException() {
    }

    // Constructor với tất cả các tham số
    public APIException(int status, String message, int EC, Object data) {
        this.status = status;
        this.message = message;
        this.EC = EC;
        this.data = data;
    }

    // Constructor với EC, message và status
    public APIException(int EC, String message, int status) {
        this.EC = EC;
        this.status = status;
        this.message = message;
    }

    // Constructor với EC và message
    public APIException(int EC, String message) {
        this.EC = EC;
        this.message = message;
    }

    // Getter và Setter
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
