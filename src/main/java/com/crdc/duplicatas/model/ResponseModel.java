package com.crdc.duplicatas.model;

import com.crdc.duplicatas.enums.Status;

import java.util.List;

public class ResponseModel {

    private Status status;
    private String message;
    private DataModel data;
    private List<ErrorModel> errors;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }
}
