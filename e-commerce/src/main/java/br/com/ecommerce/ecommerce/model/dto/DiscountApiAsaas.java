package br.com.ecommerce.ecommerce.model.dto;

public class DiscountApiAsaas {

    private float value;
    private String dueDateLimitDays;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDueDateLimitDays() {
        return dueDateLimitDays;
    }

    public void setDueDateLimitDays(String dueDateLimitDays) {
        this.dueDateLimitDays = dueDateLimitDays;
    }
}
