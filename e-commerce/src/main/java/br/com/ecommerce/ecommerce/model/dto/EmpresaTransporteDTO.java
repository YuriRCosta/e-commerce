package br.com.ecommerce.ecommerce.model.dto;

import br.com.ecommerce.ecommerce.model.dto.frete.DeliveryRange;

import java.io.Serializable;

public class EmpresaTransporteDTO implements Serializable {

    private String id;
    private String nome;
    private String valor;
    private String empresa;
    private String picture;
    private DeliveryRange delivery_range;

    public DeliveryRange getDelivery_range() {
        return delivery_range;
    }

    public void setDelivery_range(DeliveryRange delivery_range) {
        this.delivery_range = delivery_range;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
