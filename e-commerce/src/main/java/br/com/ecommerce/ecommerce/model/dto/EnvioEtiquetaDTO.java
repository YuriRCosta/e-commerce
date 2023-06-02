package br.com.ecommerce.ecommerce.model.dto;

import br.com.ecommerce.ecommerce.model.dto.frete.FromToEnvioEtiqueta;
import br.com.ecommerce.ecommerce.model.dto.frete.OptionsEnvioEtiqueta;
import br.com.ecommerce.ecommerce.model.dto.frete.ProductsEnvioEtiqueta;
import br.com.ecommerce.ecommerce.model.dto.frete.VolumeEnvioEtiqueta;

import java.io.Serializable;
import java.util.List;

public class EnvioEtiquetaDTO implements Serializable {

    private String service;
    private String agency;
    private FromToEnvioEtiqueta from = new FromToEnvioEtiqueta();
    private FromToEnvioEtiqueta to = new FromToEnvioEtiqueta();
    private List<ProductsEnvioEtiqueta> products;
    private VolumeEnvioEtiqueta volumes = new VolumeEnvioEtiqueta();
    private OptionsEnvioEtiqueta options = new OptionsEnvioEtiqueta();

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public FromToEnvioEtiqueta getFrom() {
        return from;
    }

    public void setFrom(FromToEnvioEtiqueta from) {
        this.from = from;
    }

    public FromToEnvioEtiqueta getTo() {
        return to;
    }

    public void setTo(FromToEnvioEtiqueta to) {
        this.to = to;
    }

    public List<ProductsEnvioEtiqueta> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsEnvioEtiqueta> products) {
        this.products = products;
    }

    public VolumeEnvioEtiqueta getVolumes() {

        return volumes;
    }

    public void setVolumes(VolumeEnvioEtiqueta volumes) {
        this.volumes = volumes;
    }

    public OptionsEnvioEtiqueta getOptions() {
        return options;
    }

    public void setOptions(OptionsEnvioEtiqueta options) {
        this.options = options;
    }
}
