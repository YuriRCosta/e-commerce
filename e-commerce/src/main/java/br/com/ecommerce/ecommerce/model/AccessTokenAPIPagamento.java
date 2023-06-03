package br.com.ecommerce.ecommerce.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "access_token_api_pagamento")
@SequenceGenerator(name = "seq_access_token_api_pagamento", sequenceName = "seq_access_token_api_pagamento", allocationSize = 1, initialValue = 1)
public class AccessTokenAPIPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_access_token_api_pagamento")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    private String tokenType;
    private String expiresIn;
    private String scope;
    private String jti;
    private String userName;
    private String tokenAcesso;
    
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro = Calendar.getInstance().getTime();

    public boolean expirado() {
        Date dataAtual = Calendar.getInstance().getTime();
        
        Long tempo = dataAtual.getTime() - this.dataCadastro.getTime();

        return tempo > 3600000;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokenAcesso() {
        return tokenAcesso;
    }

    public void setTokenAcesso(String tokenAcesso) {
        this.tokenAcesso = tokenAcesso;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
