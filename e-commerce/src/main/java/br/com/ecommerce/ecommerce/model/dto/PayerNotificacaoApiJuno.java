package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class PayerNotificacaoApiJuno implements Serializable {

    private String name;
    private String document;
    private AddressNotificacaoApiJuno address = new AddressNotificacaoApiJuno();
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public AddressNotificacaoApiJuno getAddress() {
        return address;
    }

    public void setAddress(AddressNotificacaoApiJuno address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
