package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class Links implements Serializable {

    private Self self = new Self();

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }
}
