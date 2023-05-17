package br.com.ecommerce.ecommerce.enums;

public enum TipoPessoa {

    JURIDICA("Jurídica"),
    FISICA("Física"),
    JURIDICA_FORNECEDOR("Jurídica e Fornecedor");

    private String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
