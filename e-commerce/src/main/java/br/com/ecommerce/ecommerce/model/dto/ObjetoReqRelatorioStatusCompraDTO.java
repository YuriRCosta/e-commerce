package br.com.ecommerce.ecommerce.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ObjetoReqRelatorioStatusCompraDTO implements Serializable {

    private String nomeProduto;

    @NotNull(message = "Informe a data inicial.")
    @NotEmpty(message = "Informe a data inicial.")
    private String dataInicial;

    @NotNull(message = "Informe a data final.")
    @NotEmpty(message = "Informe a data final.")
    private String dataFinal;
    private String codigoProduto;
    private String emailCliente;
    private String nomeCliente;
    private String foneCliente;
    private String valorVendaProduto;
    private String qtdEstoque;

    @NotNull(message = "Informe o status da venda.")
    @NotEmpty(message = "Informe o status da venda.")
    private String statusVenda;

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getFoneCliente() {
        return foneCliente;
    }

    public void setFoneCliente(String foneCliente) {
        this.foneCliente = foneCliente;
    }

    public String getValorVendaProduto() {
        return valorVendaProduto;
    }

    public void setValorVendaProduto(String valorVendaProduto) {
        this.valorVendaProduto = valorVendaProduto;
    }

    public String getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(String qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public String getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(String statusVenda) {
        this.statusVenda = statusVenda;
    }
}
