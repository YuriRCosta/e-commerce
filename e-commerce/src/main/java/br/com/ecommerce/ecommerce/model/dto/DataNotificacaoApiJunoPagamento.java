package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataNotificacaoApiJunoPagamento implements Serializable {

    private String eventId;
    private String eventType;
    private String timestamp;
    private List<AttributesNotificacaoApiJuno> data = new ArrayList<>();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<AttributesNotificacaoApiJuno> getData() {
        return data;
    }

    public void setData(List<AttributesNotificacaoApiJuno> data) {
        this.data = data;
    }
}
