package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class OptionsEnvioEtiqueta implements Serializable {

    private String insurance_value;
    private Boolean receipt;
    private Boolean own_hand;
    private Boolean reverse;
    private Boolean non_commercial;
    private Invoice invoice = new Invoice();
    private String platform;
    private Tags tags = new Tags();

    public String getInsurance_value() {
        return insurance_value;
    }

    public void setInsurance_value(String insurance_value) {
        this.insurance_value = insurance_value;
    }

    public Boolean getReceipt() {
        return receipt;
    }

    public void setReceipt(Boolean receipt) {
        this.receipt = receipt;
    }

    public Boolean getOwn_hand() {
        return own_hand;
    }

    public void setOwn_hand(Boolean own_hand) {
        this.own_hand = own_hand;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public Boolean getNon_commercial() {
        return non_commercial;
    }

    public void setNon_commercial(Boolean non_commercial) {
        this.non_commercial = non_commercial;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }
}
