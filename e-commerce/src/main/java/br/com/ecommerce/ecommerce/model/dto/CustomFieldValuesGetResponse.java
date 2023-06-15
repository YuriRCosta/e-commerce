package br.com.ecommerce.ecommerce.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomFieldValuesGetResponse {

    private String customFieldId;
    private List<String> value = new ArrayList<>();

    public String getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

}
