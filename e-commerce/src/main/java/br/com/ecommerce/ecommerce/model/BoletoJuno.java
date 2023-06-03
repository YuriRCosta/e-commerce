package br.com.ecommerce.ecommerce.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "boleto_juno")
@SequenceGenerator(name = "seq_boleto_juno", sequenceName = "seq_boleto_juno", allocationSize = 1, initialValue = 1)
public class BoletoJuno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boleto_juno")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_compra_loja_virtual_id",nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "venda_compra_loja_virtual_fk"))
    private VendaCompraLojaVirtual vendaCompraLojaVirtual;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private PessoaJuridica empresa;

    @Column(nullable = false)
    private String code;

    private String link = "";
    private String checkoutUrl = "";
    private boolean quitado = false;
    private String dataVencimento = "";
    private BigDecimal valor = BigDecimal.ZERO;
    private Integer recorrencia = 0;
    private String idChrBoleto = "";
    private String installmentLink = "";
    private String idPix = "";

    @Column(columnDefinition = "text")
    private String payloadInBase64;

    @Column(columnDefinition = "text")
    private String imageInBase64;

    private String chargeICartao = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VendaCompraLojaVirtual getVendaCompraLojaVirtual() {
        return vendaCompraLojaVirtual;
    }

    public void setVendaCompraLojaVirtual(VendaCompraLojaVirtual vendaCompraLojaVirtual) {
        this.vendaCompraLojaVirtual = vendaCompraLojaVirtual;
    }

    public PessoaJuridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PessoaJuridica empresa) {
        this.empresa = empresa;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public boolean isQuitado() {
        return quitado;
    }

    public void setQuitado(boolean quitado) {
        this.quitado = quitado;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getRecorrencia() {
        return recorrencia;
    }

    public void setRecorrencia(Integer recorrencia) {
        this.recorrencia = recorrencia;
    }

    public String getIdChrBoleto() {
        return idChrBoleto;
    }

    public void setIdChrBoleto(String idChrBoleto) {
        this.idChrBoleto = idChrBoleto;
    }

    public String getInstallmentLink() {
        return installmentLink;
    }

    public void setInstallmentLink(String installmentLink) {
        this.installmentLink = installmentLink;
    }

    public String getIdPix() {
        return idPix;
    }

    public void setIdPix(String idPix) {
        this.idPix = idPix;
    }

    public String getPayloadInBase64() {
        return payloadInBase64;
    }

    public void setPayloadInBase64(String payloadInBase64) {
        this.payloadInBase64 = payloadInBase64;
    }

    public String getImageInBase64() {
        return imageInBase64;
    }

    public void setImageInBase64(String imageInBase64) {
        this.imageInBase64 = imageInBase64;
    }

    public String getChargeICartao() {
        return chargeICartao;
    }

    public void setChargeICartao(String chargeICartao) {
        this.chargeICartao = chargeICartao;
    }
}
