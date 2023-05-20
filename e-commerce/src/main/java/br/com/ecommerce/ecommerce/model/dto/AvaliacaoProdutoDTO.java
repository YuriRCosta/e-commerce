package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class AvaliacaoProdutoDTO implements Serializable {

    private Long id;

    private Integer nota;

    private String descricao;

    private Long empresa;

    private Long pessoa;

    private Long produto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public Long getPessoa() {
        return pessoa;
    }

    public void setPessoa(Long pessoa) {
        this.pessoa = pessoa;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }
}
