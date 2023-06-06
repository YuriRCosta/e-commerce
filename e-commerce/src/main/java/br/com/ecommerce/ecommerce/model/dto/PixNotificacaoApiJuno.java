package br.com.ecommerce.ecommerce.model.dto;

import java.io.Serializable;

public class PixNotificacaoApiJuno implements Serializable {

    private String txid;
    private String endToEndId;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }
}
