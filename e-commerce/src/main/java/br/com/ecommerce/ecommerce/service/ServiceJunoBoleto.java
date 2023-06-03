package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.AccessTokenAPIPagamento;
import br.com.ecommerce.ecommerce.repository.AccessTokenJunoRepository;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.Serializable;

@Service
public class ServiceJunoBoleto implements Serializable {

    @Autowired
    private AccessTokenJunoRepository accessTokenJunoRepository;

    @Autowired
    private AccessTokenJunoService accessTokenJunoService;

    public AccessTokenAPIPagamento buscaTokenAtivo() throws Exception {
        AccessTokenAPIPagamento accessTokenAPIPagamento = accessTokenJunoService.buscaTokenAtivo();

        if (accessTokenAPIPagamento == null || accessTokenAPIPagamento.expirado()) {
            String clientId = "vi7QZerW09C8JG1o";
            String secretId = "$2a$10$";

            Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();

            WebResource webResource = client.resource("https://sandbox.boletobancario.com/authorization-server/oauth/token?grant_type=client_credentials");

            String basicChave = clientId + ":" + secretId;
            String tokenAutenticacao = DatatypeConverter.printBase64Binary(basicChave.getBytes());

            ClientResponse response = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED).type(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + tokenAutenticacao)
                    .post(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Erro ao buscar o token: " + response.getStatus());
            }

            accessTokenJunoRepository.deleteAll();
            accessTokenJunoRepository.flush();

            AccessTokenAPIPagamento accessTokenAPIPagamentoNovo = response.getEntity(AccessTokenAPIPagamento.class);
            accessTokenAPIPagamentoNovo.setTokenAcesso(tokenAutenticacao);

            return accessTokenJunoRepository.save(accessTokenAPIPagamentoNovo);

        } else {
            return accessTokenAPIPagamento;
        }
    }

}
