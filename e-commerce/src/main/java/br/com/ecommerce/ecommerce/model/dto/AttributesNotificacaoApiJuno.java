package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class AttributesNotificacaoApiJuno implements Serializable {

   private String entityType;
   private String entityId;
   private Attributes2 attributes = new Attributes2();

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Attributes2 getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes2 attributes) {
        this.attributes = attributes;
    }
}


