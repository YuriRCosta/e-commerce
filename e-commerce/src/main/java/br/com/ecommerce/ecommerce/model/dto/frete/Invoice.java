package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class Invoice implements Serializable {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
