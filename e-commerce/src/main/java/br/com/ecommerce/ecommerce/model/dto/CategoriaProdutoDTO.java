package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class CategoriaProdutoDTO implements Serializable {

    private String nomeDesc;
    private Long id;
    private String empresa;

    public String getNomeDesc() {
        return nomeDesc;
    }

    public void setNomeDesc(String nomeDesc) {
        this.nomeDesc = nomeDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}
