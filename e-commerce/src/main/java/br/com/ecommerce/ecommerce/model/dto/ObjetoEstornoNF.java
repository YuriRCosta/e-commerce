package br.com.ecommerce.ecommerce.model.dto;

public class ObjetoEstornoNF {

    private String chave;
    private String natureza_operacao;
    private String codigo_cfop;
    private String ambiente;

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNatureza_operacao() {
        return natureza_operacao;
    }

    public void setNatureza_operacao(String natureza_operacao) {
        this.natureza_operacao = natureza_operacao;
    }

    public String getCodigo_cfop() {
        return codigo_cfop;
    }

    public void setCodigo_cfop(String codigo_cfop) {
        this.codigo_cfop = codigo_cfop;
    }

}
