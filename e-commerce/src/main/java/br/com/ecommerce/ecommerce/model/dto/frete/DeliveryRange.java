package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class DeliveryRange implements Serializable {

    private String min;
    private String max;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
