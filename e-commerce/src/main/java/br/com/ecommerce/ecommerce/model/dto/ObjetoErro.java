package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class ObjetoErro implements Serializable {

    private String error;

    private String code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
