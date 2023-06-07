package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CriarWebHook implements Serializable {

    private String url;
    private List<String> eventTypes = new ArrayList<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
