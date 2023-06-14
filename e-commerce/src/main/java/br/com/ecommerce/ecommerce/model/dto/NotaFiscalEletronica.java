package br.com.ecommerce.ecommerce.model.dto;

import java.util.ArrayList;
import java.util.List;

public class NotaFiscalEletronica {

    private String ID;

    private String url_notificacao;

    private Integer operacao;
    private String natureza_operacao;
    private String modelo;
    private Integer finalidade;
    private Integer ambiente;
    private WebManiaClienteNF cliente = new WebManiaClienteNF();
    private List<ProdutoNF> produtos = new ArrayList<>();
    private PedidoNF pedido = new PedidoNF();

    public WebManiaClienteNF getCliente() {
        return cliente;
    }

    public void setCliente(WebManiaClienteNF cliente) {
        this.cliente = cliente;
    }

    public List<ProdutoNF> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoNF> produtos) {
        this.produtos = produtos;
    }

    public PedidoNF getPedido() {
        return pedido;
    }

    public void setPedido(PedidoNF pedido) {
        this.pedido = pedido;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getUrl_notificacao() {
        return url_notificacao;
    }

    public void setUrl_notificacao(String url_notificacao) {
        this.url_notificacao = url_notificacao;
    }

    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    public String getNatureza_operacao() {
        return natureza_operacao;
    }

    public void setNatureza_operacao(String natureza_operacao) {
        this.natureza_operacao = natureza_operacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(Integer finalidade) {
        this.finalidade = finalidade;
    }

    public Integer getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Integer ambiente) {
        this.ambiente = ambiente;
    }


}
