package br.com.ecommerce.ecommerce.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ErroResponseAsaas {

    private List<ObjetoErroResponseAsaas> errors = new ArrayList<>();

    public List<ObjetoErroResponseAsaas> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjetoErroResponseAsaas> errors) {
        this.errors = errors;
    }

    public String listaErros() {
        StringBuilder sb = new StringBuilder();

        for (ObjetoErroResponseAsaas erro : errors) {
            sb.append(erro.getCode());
            sb.append(" - ");
            sb.append(erro.getDescription());
            sb.append("\n");
        }

        return sb.toString();
    }
}
