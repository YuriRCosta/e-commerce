package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class To implements Serializable {

    private String postal_code;

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
}
