package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Embedded implements Serializable {

    private List<ConteudoBoletoJuno> charges = new ArrayList<>();

    public List<ConteudoBoletoJuno> getCharges() {
        return charges;
    }

    public void setCharges(List<ConteudoBoletoJuno> charges) {
        this.charges = charges;
    }
}
