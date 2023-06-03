package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoletoGeradoApiJuno implements Serializable {

    private List<Links> _links = new ArrayList<>();

    private Embedded _embedded = new Embedded();

    public Embedded get_embedded() {
        return _embedded;
    }

    public void set_embedded(Embedded _embedded) {
        this._embedded = _embedded;
    }

    public List<Links> get_links() {
        return _links;
    }

    public void set_links(List<Links> _links) {
        this._links = _links;
    }
}
