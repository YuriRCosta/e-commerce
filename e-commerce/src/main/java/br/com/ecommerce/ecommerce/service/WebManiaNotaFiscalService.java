package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.NotaFiscalVenda;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.model.dto.NotaFiscalEletronica;
import br.com.ecommerce.ecommerce.model.dto.ObjetoDevolucaoNF;
import br.com.ecommerce.ecommerce.model.dto.ObjetoEmissaoNotaFiscal;
import br.com.ecommerce.ecommerce.model.dto.ObjetoEstornoNF;
import br.com.ecommerce.ecommerce.repository.NotaFiscalVendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebManiaNotaFiscalService {

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    public String devolucaoNotaFiscal(ObjetoDevolucaoNF objetoDevolucaoNF) throws Exception {
        Client client = new HostIgnoringClient("https://webmaniabr.com/api/").hostIgnoringClient();
        WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/devolucao/");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(objetoDevolucaoNF);

        ClientResponse response = webResource
                .header("Content-Type", "application/json")
                .header("X-Consumer-Key", "SEU_CONSUMER_KEY")
                .header("X-Consumer-Secret", "SEU_CONSUMER_SECRET")
                .header("X-Access-Token", "SEU_ACCESS_TOKEN")
                .header("X-Access-Token-Secret", "SEU_TOKEN_SECRET")
                .post(ClientResponse.class, json);

        return response.getEntity(String.class);
    }

    public String estornoNotaFiscal(ObjetoEstornoNF objetoEstornoNF) throws Exception {
        Client client = new HostIgnoringClient("https://webmaniabr.com/api/").hostIgnoringClient();
        WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/devolucao/");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(objetoEstornoNF);

        ClientResponse response = webResource
                .header("Content-Type", "application/json")
                .header("X-Consumer-Key", "SEU_CONSUMER_KEY")
                .header("X-Consumer-Secret", "SEU_CONSUMER_SECRET")
                .header("X-Access-Token", "SEU_ACCESS_TOKEN")
                .header("X-Access-Token-Secret", "SEU_TOKEN_SECRET")
                .post(ClientResponse.class, json);

        return response.getEntity(String.class);
    }

    public NotaFiscalVenda gravaNFParaVenda(ObjetoEmissaoNotaFiscal objetoEmissaoNotaFiscal, VendaCompraLojaVirtual vendaCompraLojaVirtual) {
        NotaFiscalVenda notaFiscalVendaBusca = notaFiscalVendaRepository.findByVendaId(vendaCompraLojaVirtual.getId());
        NotaFiscalVenda notaFiscalVenda = new NotaFiscalVenda();

        if (notaFiscalVendaBusca != null && notaFiscalVendaBusca.getId() > 0) {
            notaFiscalVenda.setId(notaFiscalVendaBusca.getId());
        }

        notaFiscalVenda.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        notaFiscalVenda.setNumeroNota(objetoEmissaoNotaFiscal.getUuid());
        notaFiscalVenda.setPdf(objetoEmissaoNotaFiscal.getDanfe());
        notaFiscalVenda.setXml(objetoEmissaoNotaFiscal.getXml());
        notaFiscalVenda.setTipo(objetoEmissaoNotaFiscal.getMotivo());
        notaFiscalVenda.setNumeroSerie(objetoEmissaoNotaFiscal.getSerie());
        notaFiscalVenda.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

        return notaFiscalVendaRepository.saveAndFlush(notaFiscalVenda);
    }

    public String consultaNotaFiscal(String uuid) throws Exception {
        Client client = new HostIgnoringClient("https://webmaniabr.com/api/").hostIgnoringClient();
        WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/consulta/");

        ClientResponse response = webResource.queryParam("uuid", uuid)
                .header("Content-Type", "application/json")
                .header("X-Consumer-Key", "SEU_CONSUMER_KEY")
                .header("X-Consumer-Secret", "SEU_CONSUMER_SECRET")
                .header("X-Access-Token", "SEU_ACCESS_TOKEN")
                .header("X-Access-Token-Secret", "SEU_TOKEN_SECRET")
                .get(ClientResponse.class);

        return response.getEntity(String.class);
    }

    public String cancelarNotaFiscal(String uuid, String motivo) throws Exception {
        Client client = new HostIgnoringClient("https://webmaniabr.com/api/").hostIgnoringClient();
        WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/cancelar/");

        String json = "{\"uuid\":\"" + uuid + "\",\"motivo\":\"" + motivo + "\"}";

        ClientResponse response = webResource
                .header("Content-Type", "application/json")
                .header("X-Consumer-Key", "SEU_CONSUMER_KEY")
                .header("X-Consumer-Secret", "SEU_CONSUMER_SECRET")
                .header("X-Access-Token", "SEU_ACCESS_TOKEN")
                .header("X-Access-Token-Secret", "SEU_TOKEN_SECRET")
                .put(ClientResponse.class, json);

        return response.getEntity(String.class);
    }

    public String emitirNotaFiscal(NotaFiscalEletronica notaFiscalEletronica) throws Exception {
        Client client = new HostIgnoringClient("https://webmaniabr.com/api/").hostIgnoringClient();
        WebResource webResource = client.resource("https://webmaniabr.com/api/1/nfe/emissao/");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(notaFiscalEletronica);

        ClientResponse response = webResource
                .header("Content-Type", "application/json")
                .header("X-Consumer-Key", "SEU_CONSUMER_KEY")
                .header("X-Consumer-Secret", "SEU_CONSUMER_SECRET")
                .header("X-Access-Token", "SEU_ACCESS_TOKEN")
                .header("X-Access-Token-Secret", "SEU_TOKEN_SECRET")
                .post(ClientResponse.class, json);

        return response.getEntity(String.class);
    }

}
