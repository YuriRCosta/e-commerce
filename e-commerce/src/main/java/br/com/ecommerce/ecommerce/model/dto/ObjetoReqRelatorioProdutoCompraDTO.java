package br.com.ecommerce.ecommerce.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ObjetoReqRelatorioProdutoCompraDTO implements Serializable {

    private String nomeProduto;

    @NotNull(message = "Informe a data inicial.")
    @NotEmpty(message = "Informe a data inicial.")
    private String dataInicial;

    @NotNull(message = "Informe a data final.")
    @NotEmpty(message = "Informe a data final.")
    private String dataFinal;
    private String codigoProduto;
    private String codigoNota;
    private String valorVendaProduto;
    private String nomeFornecedor;
    private String codigoFornecedor;
    private String quantidadeComprada;
    private String dataCompra;

    public String getValorVendaProduto() {
        return valorVendaProduto;
    }

    public void setValorVendaProduto(String valorVendaProduto) {
        this.valorVendaProduto = valorVendaProduto;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(String codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    public String getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(String quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

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

    public String getCodigoNota() {
        return codigoNota;
    }

    public void setCodigoNota(String codigoNota) {
        this.codigoNota = codigoNota;
    }
}
