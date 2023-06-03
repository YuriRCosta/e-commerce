package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class CobrancaJunoApi implements Serializable {

    private Charge charge = new Charge();

    private BillingJuno billing = new BillingJuno();

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public BillingJuno getBilling() {
        return billing;
    }

    public void setBilling(BillingJuno billingJuno) {
        this.billing = billingJuno;
    }
}
