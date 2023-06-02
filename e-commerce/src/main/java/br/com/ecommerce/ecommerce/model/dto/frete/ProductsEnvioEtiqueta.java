package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class ProductsEnvioEtiqueta implements Serializable {

    private String id;
    private String quantity;
    private String unitary_value;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitary_value() {
        return unitary_value;
    }

    public void setUnitary_value(String unitary_value) {
        this.unitary_value = unitary_value;
    }
}
