package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class VolumeEnvioEtiqueta implements Serializable {

    private String weight;
    private String width;
    private String height;
    private String length;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
