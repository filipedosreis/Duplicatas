package com.crdc.duplicatas.exception;

import com.crdc.duplicatas.model.ErrorModel;

import java.util.List;

public class ValidacaoException extends RuntimeException {

    private List<ErrorModel> errors;

    public ValidacaoException(String mensagem, List<ErrorModel> errors) {
        super(mensagem);
        this.errors = errors;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }
}
