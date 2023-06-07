package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import br.com.ecommerce.ecommerce.model.AccessTokenAPIPagamento;
import br.com.ecommerce.ecommerce.model.BoletoJuno;
import br.com.ecommerce.ecommerce.model.dto.*;
import br.com.ecommerce.ecommerce.repository.BoletoJunoRepository;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.repository.AccessTokenJunoRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ServiceJunoBoleto implements Serializable {

    @Autowired
    private AccessTokenJunoRepository accessTokenJunoRepository;

    @Autowired
    private AccessTokenJunoService accessTokenJunoService;

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    @Autowired
    private BoletoJunoRepository boletoJunoRepository;

    public String gerarCarneApi(ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(objetoPostCarneJuno.getIdVenda()).get();

        CobrancaJunoApi cobrancaJunoApi = new CobrancaJunoApi();

        cobrancaJunoApi.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
        cobrancaJunoApi.getCharge().setDescription(objetoPostCarneJuno.getDescription());
        cobrancaJunoApi.getCharge().setAmount(Float.valueOf(objetoPostCarneJuno.getTotalAmount()));
        cobrancaJunoApi.getCharge().setInstallments(objetoPostCarneJuno.getInstallments());

        Calendar dataVencimento = Calendar.getInstance();
        dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cobrancaJunoApi.getCharge().setDueDate(simpleDateFormat.format(dataVencimento.getTime()));

        cobrancaJunoApi.getCharge().setFine(BigDecimal.valueOf(1.00));
        cobrancaJunoApi.getCharge().setInterest(BigDecimal.valueOf(1.00));
        cobrancaJunoApi.getCharge().setMaxOverdueDays(10);
        cobrancaJunoApi.getCharge().getPaymentTypes().add("BOLETO_PIX");

        cobrancaJunoApi.getBilling().setEmail(objetoPostCarneJuno.getEmail());
        cobrancaJunoApi.getBilling().setDocument(objetoPostCarneJuno.getPayerCpfCnpj());
        cobrancaJunoApi.getBilling().setName(objetoPostCarneJuno.getPayerName());
        cobrancaJunoApi.getBilling().setPhone(objetoPostCarneJuno.getPayerPhone());

        AccessTokenAPIPagamento accessTokenAPIPagamento = buscaTokenAtivo();
        if (accessTokenAPIPagamento != null) {
            Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();
            WebResource webResource = client.resource("https://sandbox.boletobancario.com/api-integration/payments");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(accessTokenAPIPagamento);

            ClientResponse response = webResource
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Content-Type", "application/json")
                    .header("X-Api-Version", "2")
                    .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                    .header("X-Idempotence-Key", "chave-boleto-pix")
                    .header("Authorization", "Bearer " + accessTokenAPIPagamento.getTokenAcesso())
                    .post(ClientResponse.class, cobrancaJunoApi);

            String retorno = response.getEntity(String.class);

            if (response.getStatus() == 200) {
                response.close();
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

                BoletoGeradoApiJuno jsonRetornoObj = objectMapper.readValue(retorno, new TypeReference<BoletoGeradoApiJuno>() {});

                List<BoletoJuno> boletoJunos = new ArrayList<>();

                int recorrencia = 1;
                for (ConteudoBoletoJuno c : jsonRetornoObj.get_embedded().getCharges()) {
                    BoletoJuno boletoJuno = new BoletoJuno();
                    boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
                    boletoJuno.setCode(c.getCode());
                    boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
                    boletoJuno.setLink(c.getLink());
                    boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
                    boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
                    boletoJuno.setValor(new BigDecimal(c.getAmount()));
                    boletoJuno.setIdChrBoleto(c.getId());
                    boletoJuno.setInstallmentLink(c.getInstallmentLink());
                    boletoJuno.setIdPix(c.getPix().getId());
                    boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
                    boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
                    boletoJuno.setRecorrencia(recorrencia);

                    boletoJunos.add(boletoJuno);

                    recorrencia++;
                }

                boletoJunoRepository.saveAllAndFlush(boletoJunos);

                return boletoJunos.get(0).getLink();
            } else {
                return retorno;
            }
        } else {
            throw new Exception("Não exite chave de acesso para a API");
        }
    }

    public String geraChaveBoletoPix() throws Exception {
        AccessTokenAPIPagamento accessTokenAPIPagamento = buscaTokenAtivo();
        Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();
        WebResource webResource = client.resource("https://sandbox.boletobancario.com/charges");

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Api-Version", "2")
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                //.header("X-Idempotence-Key", "chave-boleto-pix")
                .header("Authorization", "Bearer " + accessTokenAPIPagamento.getTokenAcesso())
                .post(ClientResponse.class, "{ \"type\": \"RANDOM_KEY\" }");

        return response.getEntity(String.class);
    }

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

    public String cancelarBoleto(String code) throws Exception {
        AccessTokenAPIPagamento accessTokenAPIPagamento = buscaTokenAtivo();

        Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();
        WebResource webResource = client.resource("https://sandbox.boletobancario.com/api-integration/charges/" + code + "/cancelation");

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Api-Version", "2")
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenAPIPagamento.getTokenAcesso())
                .put(ClientResponse.class);

        if (response.getStatus() == 204) {
            return "Boleto cancelado com sucesso";
        } else {
            return response.getEntity(String.class);
        }
    }

    public String criarWebHook(CriarWebHook criarWebHook) throws Exception {
        AccessTokenAPIPagamento accessTokenAPIPagamento = buscaTokenAtivo();
        Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();
        WebResource webResource = client.resource("https://sandbox.boletobancario.com/notifications/webhooks");

        String json = new ObjectMapper().writeValueAsString(criarWebHook);

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Api-Version", "2")
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenAPIPagamento.getTokenAcesso())
                .post(ClientResponse.class, json);

        String retorno = response.getEntity(String.class);
        response.close();

        return retorno;
    }

    public String listaWebHook() throws Exception {
        AccessTokenAPIPagamento accessTokenAPIPagamento = buscaTokenAtivo();
        Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();
        WebResource webResource = client.resource("https://sandbox.boletobancario.com/notifications/webhooks");

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Api-Version", "2")
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenAPIPagamento.getTokenAcesso())
                .get(ClientResponse.class);

        String retorno = response.getEntity(String.class);

        return retorno;
    }

    public void deleteWebHook() throws Exception {
        AccessTokenAPIPagamento accessTokenAPIPagamento = buscaTokenAtivo();
        Client client = new HostIgnoringClient("sandbox.boletobancario.com").hostIgnoringClient();
        WebResource webResource = client.resource("https://sandbox.boletobancario.com/notifications/webhooks");

        webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Api-Version", "2")
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenAPIPagamento.getTokenAcesso())
                .delete();

    }

}
