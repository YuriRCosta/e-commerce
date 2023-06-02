package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;

public class DimensionsDTO implements Serializable {

    private String width;
    private String height;
    private String length;

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
