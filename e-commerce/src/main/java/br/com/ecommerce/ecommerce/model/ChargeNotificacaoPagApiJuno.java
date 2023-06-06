package br.com.ecommerce.ecommerce.model;

import br.com.ecommerce.ecommerce.model.dto.PayerNotificacaoApiJuno;

import java.io.Serializable;

public class ChargeNotificacaoPagApiJuno implements Serializable {

    private String id;
    private String code;
    private String dueDate;
    private String status;
    private String amount;

    private PayerNotificacaoApiJuno payer = new PayerNotificacaoApiJuno();

    public PayerNotificacaoApiJuno getPayer() {
        return payer;
    }

    public void setPayer(PayerNotificacaoApiJuno payer) {
        this.payer = payer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
