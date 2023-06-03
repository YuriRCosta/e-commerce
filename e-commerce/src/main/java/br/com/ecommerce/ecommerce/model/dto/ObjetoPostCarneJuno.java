package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class ObjetoPostCarneJuno implements Serializable {

    private String description;
    private String payerName;
    private String payerPhone;
    private String totalAmount;
    private String installments;
    private String reference;
    private Integer recurrency = 0;
    private String payerCpfCnpj;
    private String email;
    private Long idVenda;

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPayerCpfCnpj() {
        return payerCpfCnpj;
    }

    public void setPayerCpfCnpj(String payerCpfCnpj) {
        this.payerCpfCnpj = payerCpfCnpj;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInstallments() {
        return installments;
    }

    public void setInstallments(String installments) {
        this.installments = installments;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getRecurrency() {
        return recurrency;
    }

    public void setRecurrency(Integer recurrency) {
        this.recurrency = recurrency;
    }
}
