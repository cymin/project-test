package com.example.domain;

public class ResultInfo<T>{
    private Integer status;
    private String message;
    private T response;
    // 省略其他代码...

    public ResultInfo() {
    }

    public ResultInfo(Integer status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }

    public static <T> ResultInfo success(Integer status, String message, T response) {
        return new ResultInfo(status, message, response);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
