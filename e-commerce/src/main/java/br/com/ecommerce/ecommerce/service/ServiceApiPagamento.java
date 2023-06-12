package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import br.com.ecommerce.ecommerce.model.AccessTokenAPIPagamento;
import br.com.ecommerce.ecommerce.model.BoletoJuno;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.model.dto.*;
import br.com.ecommerce.ecommerce.repository.AccessTokenJunoRepository;
import br.com.ecommerce.ecommerce.repository.BoletoJunoRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import br.com.ecommerce.ecommerce.util.ValidaCPF;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import jakarta.xml.bind.DatatypeConverter;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ServiceApiPagamento implements Serializable {

    @Autowired
    private AccessTokenJunoRepository accessTokenJunoRepository;

    @Autowired
    private AccessTokenJunoService accessTokenJunoService;

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private BoletoJunoRepository boletoJunoRepository;

    public ObjetoQrCodePix buscarQrCodePixAsaas(String idCobranca) throws Exception {
        Client client = new HostIgnoringClient(AsaasApiStatus.ULR_API_ASAAS).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiStatus.ULR_API_ASAAS + "payments/" + idCobranca + "/pixQrCode");

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("access_token", AsaasApiStatus.API_KEY)
                .get(ClientResponse.class);

        String retorno = response.getEntity(String.class);

        ObjetoQrCodePix objetoQrCodePix = new ObjetoQrCodePix();

        LinkedHashMap<String, Object> parser = new JSONParser(retorno).parseObject();
        objetoQrCodePix.setEncodedImage((String) parser.get("encodedImage"));
        objetoQrCodePix.setPayload((String) parser.get("payload"));

        return objetoQrCodePix;
    }

    public String gerarCarneAsaas(ObjetoPostCarneJuno dados) throws Exception {
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(dados.getIdVenda()).get();

        CobrancaApiAsaas cobrancaApiAsaas = new CobrancaApiAsaas();
        cobrancaApiAsaas.setCustomer(this.buscaClienteAsaas(dados));
        cobrancaApiAsaas.setBillingType("UNDEFINED");
        cobrancaApiAsaas.setDescription("Pix ou Boleto gerado para a compra de " + vendaCompraLojaVirtual.getPessoa().getNome());
        cobrancaApiAsaas.setInstallmentValue(vendaCompraLojaVirtual.getValorTotal().floatValue());
        cobrancaApiAsaas.setInstallmentCount(1);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        cobrancaApiAsaas.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        cobrancaApiAsaas.getInterest().setValue(1F);
        cobrancaApiAsaas.getFine().setValue(1F);

        String json = new ObjectMapper().writeValueAsString(cobrancaApiAsaas);
        Client client = new HostIgnoringClient(AsaasApiStatus.ULR_API_ASAAS).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiStatus.ULR_API_ASAAS + "payments");

        ClientResponse response = webResource.accept("application/json; charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("access_token", AsaasApiStatus.API_KEY)
                .post(ClientResponse.class, json);

        String retorno = response.getEntity(String.class);

        LinkedHashMap<String, Object> map = new JSONParser(retorno).parseObject();
        String installment = map.get("installment").toString();
        Client client2 = new HostIgnoringClient(AsaasApiStatus.ULR_API_ASAAS).hostIgnoringClient();
        WebResource webResource2 = client2.resource(AsaasApiStatus.ULR_API_ASAAS + "payments?installment=" + installment);
        ClientResponse response2 = webResource2.accept("application/json; charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("access_token", AsaasApiStatus.API_KEY)
                .get(ClientResponse.class);

        String retorno2 = response2.getEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        CobrancaGeradaAsaas listaCobranca = mapper.readValue(retorno2, new TypeReference<CobrancaGeradaAsaas>() {});

        List<BoletoJuno> boletosJuno = new ArrayList<>();

        int recorrencia = 1;
        for (DataCobrancaGeradaAsaas data : listaCobranca.getData()) {
            BoletoJuno boletoJuno = new BoletoJuno();

            boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            boletoJuno.setCode(data.getId());
            boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
            boletoJuno.setLink(data.getInvoiceUrl());
            boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(data.getDueDate())));
            boletoJuno.setCheckoutUrl(data.getInvoiceUrl());
            boletoJuno.setValor(new BigDecimal(data.getValue()));
            boletoJuno.setIdChrBoleto(data.getId());
            boletoJuno.setInstallmentLink(data.getInvoiceUrl());
            boletoJuno.setRecorrencia(recorrencia);

            ObjetoQrCodePix objetoQrCodePix = this.buscarQrCodePixAsaas(data.getId());

            //boletoJuno.setIdPix(c.getPix().getId());
            boletoJuno.setPayloadInBase64(objetoQrCodePix.getPayload());
            boletoJuno.setImageInBase64(objetoQrCodePix.getEncodedImage());

            boletosJuno.add(boletoJuno);
            recorrencia++;
        }
        boletoJunoRepository.saveAllAndFlush(boletosJuno);

        return boletosJuno.get(0).getCheckoutUrl();
    }

    public String buscaClienteAsaas(ObjetoPostCarneJuno dados) throws Exception {
        String customer_id= "";

        Client client = new HostIgnoringClient(AsaasApiStatus.ULR_API_ASAAS).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiStatus.ULR_API_ASAAS + "customers?email=" + dados.getEmail());

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("access_token", AsaasApiStatus.API_KEY)
                .get(ClientResponse.class);

        LinkedHashMap<String, Object> parser = new JSONParser(response.getEntity(String.class)).parseObject();
        Integer total = Integer.parseInt(parser.get("totalCount").toString());

        if (total <= 0) {
            ClienteAsaas clienteAsaas = new ClienteAsaas();
            if (!ValidaCPF.isCPF(dados.getPayerCpfCnpj())) {
                throw new Exception("CPF inválido");
            } else {
                clienteAsaas.setCpfCnpj(dados.getPayerCpfCnpj());
            }

            clienteAsaas.setName(dados.getPayerName());
            clienteAsaas.setEmail(dados.getEmail());
            clienteAsaas.setPhone(dados.getPayerPhone());

            Client client2 = new HostIgnoringClient(AsaasApiStatus.ULR_API_ASAAS).hostIgnoringClient();
            WebResource webResource2 = client2.resource(AsaasApiStatus.ULR_API_ASAAS + "customers");

            ClientResponse response2 = webResource2.accept(MediaType.APPLICATION_JSON)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("access_token", AsaasApiStatus.API_KEY)
                    .post(ClientResponse.class, new ObjectMapper().writeValueAsBytes(clienteAsaas));

            LinkedHashMap<String, Object> parser2 = new JSONParser(response2.getEntity(String.class)).parseObject();
            customer_id = (String) parser2.get("id");
        } else {
            List<Object> data = (List<Object>) parser.get("data");
            customer_id = new Gson().toJsonTree(data.get(0)).getAsJsonObject().get("id").getAsString().replaceAll("\"", "");
        }

        return customer_id;
    }

    public String criarChaveApiAsaas() throws Exception {
        Client client = new HostIgnoringClient(AsaasApiStatus.ULR_API_ASAAS).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiStatus.ULR_API_ASAAS + "pix/addressKeys");
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("access_token", AsaasApiStatus.API_KEY)
                .post(ClientResponse.class, "{\"type\":\"EVP\"}");

        return response.getEntity(String.class);
    }

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
