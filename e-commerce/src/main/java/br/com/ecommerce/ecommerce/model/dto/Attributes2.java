package br.com.ecommerce.ecommerce.model.dto;

import br.com.ecommerce.ecommerce.model.ChargeNotificacaoPagApiJuno;

import java.io.Serializable;

public class Attributes2 implements Serializable {

    private String createdOn;
    private String date;
    private String realeaseDate;
    private String amount;
    private String fee;
    private String status;
    private String type;

    private ChargeNotificacaoPagApiJuno charge = new ChargeNotificacaoPagApiJuno();

    private String digitalAccountId;

    private PixNotificacaoApiJuno pix = new PixNotificacaoApiJuno();

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRealeaseDate() {
        return realeaseDate;
    }

    public void setRealeaseDate(String realeaseDate) {
        this.realeaseDate = realeaseDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ChargeNotificacaoPagApiJuno getCharge() {
        return charge;
    }

    public void setCharge(ChargeNotificacaoPagApiJuno charge) {
        this.charge = charge;
    }

    public String getDigitalAccountId() {
        return digitalAccountId;
    }

    public void setDigitalAccountId(String digitalAccountId) {
        this.digitalAccountId = digitalAccountId;
    }

    public PixNotificacaoApiJuno getPix() {
        return pix;
    }

    public void setPix(PixNotificacaoApiJuno pix) {
        this.pix = pix;
    }
}
