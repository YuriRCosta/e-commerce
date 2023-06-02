package br.com.ecommerce.ecommerce.model.dto.frete;

import java.io.Serializable;
import java.util.List;

public class Packages implements Serializable {

    private String price;
    private String discount;
    private String format;
    private String weight;
    private String insurence_value;
    private List<ProductsDTO> products;
    private DimensionsDTO dimensions;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInsurence_value() {
        return insurence_value;
    }

    public void setInsurence_value(String insurence_value) {
        this.insurence_value = insurence_value;
    }

    public List<ProductsDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsDTO> products) {
        this.products = products;
    }

    public DimensionsDTO getDimensions() {
        return dimensions;
    }

    public void setDimensions(DimensionsDTO dimensions) {
        this.dimensions = dimensions;
    }
}
