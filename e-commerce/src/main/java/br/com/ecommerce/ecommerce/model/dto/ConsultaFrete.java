package br.com.ecommerce.ecommerce.model.dto;

import br.com.ecommerce.ecommerce.model.dto.frete.From;
import br.com.ecommerce.ecommerce.model.dto.frete.ProductsDTO;
import br.com.ecommerce.ecommerce.model.dto.frete.To;

import java.io.Serializable;
import java.util.List;

public class ConsultaFrete implements Serializable {

    private From from;
    private To to;
    private List<ProductsDTO> products;

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public List<ProductsDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsDTO> products) {
        this.products = products;
    }
}
