package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import br.com.ecommerce.ecommerce.model.dto.CampanhaGetResponse;
import br.com.ecommerce.ecommerce.model.dto.FromField;
import br.com.ecommerce.ecommerce.model.dto.LeadCampanhaGetResponse;
import br.com.ecommerce.ecommerce.model.dto.NewsLetterGetResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GetResponseService {

    public List<CampanhaGetResponse> carregarCampanha() throws Exception{

        Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE).hostIgnoringClient();
        WebResource webResource = client.resource(ApiTokenIntegracao.URL_GET_RESPONSE + "campaigns");

        String response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
                .get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return mapper.readValue(response, new TypeReference<>(){});
    }

    public String criarLead(LeadCampanhaGetResponse leadCampanhaGetResponse) throws Exception {
        Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE).hostIgnoringClient();
        WebResource webResource = client.resource(ApiTokenIntegracao.URL_GET_RESPONSE + "contacts");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(leadCampanhaGetResponse);

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
                .post(ClientResponse.class, json);

        if (response.getStatus() == 202) {
           return "Lead criado com sucesso!";
        }

        return response.getEntity(String.class);
    }

    public String enviarEmail(String campanhaId, String nomeEmail, String msg) throws Exception {
        NewsLetterGetResponse newsLetterGetResponse = new NewsLetterGetResponse();
        newsLetterGetResponse.getSendSettings().getSelectedCampaigns().add(campanhaId);/* qKBgP - Campanha e lista de e-mail para qual será enviado o e-mail*/
        newsLetterGetResponse.setSubject(nomeEmail);
        newsLetterGetResponse.setName(newsLetterGetResponse.getSubject());
        newsLetterGetResponse.getReplyTo().setFromFieldId("BCFTv");/*ID email para resposta*/
        newsLetterGetResponse.getFromField().setFromFieldId("BCFTv");/*ID do e-mail do remetente*/
        newsLetterGetResponse.getCampaign().setCampaignId("qKBgP");/*Campanha de origem, campanha pai*/

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate hoje = LocalDate.now();
        LocalDate amanha = hoje.plusDays(1);
        String dataEnvio = amanha.format(dateTimeFormatter);

        newsLetterGetResponse.setSendOn(dataEnvio + "T15:20:52-03:00");

        newsLetterGetResponse.getContent().setHtml(msg);

        String json = new ObjectMapper().writeValueAsString(newsLetterGetResponse);

        Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE).hostIgnoringClient();
        WebResource webResource = client.resource(ApiTokenIntegracao.URL_GET_RESPONSE + "newsletters");

        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
                .post(ClientResponse.class, json);

        if (response.getStatus() == 201) {
            return "E-mail enviado com sucesso!";
        }

        return response.getEntity(String.class);
    }

    public List<FromField> buscaFromField() throws Exception {
        Client client = new HostIgnoringClient(ApiTokenIntegracao.URL_GET_RESPONSE).hostIgnoringClient();
        WebResource webResource = client.resource(ApiTokenIntegracao.URL_GET_RESPONSE + "from-fields");

        String response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("X-Auth-Token", ApiTokenIntegracao.TOKEN_GET_RESPONSE)
                .get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return mapper.readValue(response, new TypeReference<>(){});
    }
}
