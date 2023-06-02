package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import br.com.ecommerce.ecommerce.enums.StatusContaReceber;
import br.com.ecommerce.ecommerce.model.*;
import br.com.ecommerce.ecommerce.model.dto.ConsultaFrete;
import br.com.ecommerce.ecommerce.model.dto.EmpresaTransporteDTO;
import br.com.ecommerce.ecommerce.model.dto.EnvioEtiquetaDTO;
import br.com.ecommerce.ecommerce.model.dto.frete.ProductsEnvioEtiqueta;
import br.com.ecommerce.ecommerce.model.dto.frete.VolumeEnvioEtiqueta;
import br.com.ecommerce.ecommerce.repository.ContaReceberRepository;
import br.com.ecommerce.ecommerce.repository.NotaFiscalVendaRepository;
import br.com.ecommerce.ecommerce.repository.StatusRastreioRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import br.com.ecommerce.ecommerce.service.ServiceSendEmail;
import br.com.ecommerce.ecommerce.service.VendaCompraLojaVirtualService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class VendaCompraLojaVirtualController {

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;
    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @PostMapping("/salvarVendaCompraLojaVirtual")
    public ResponseEntity<VendaCompraLojaVirtual> salvarVendaCompraLojaVirtual(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws MessagingException, UnsupportedEncodingException {

        for (int i = 0; i < vendaCompraLojaVirtual.getItensVendaLoja().size(); i++) {
            vendaCompraLojaVirtual.getItensVendaLoja().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItensVendaLoja().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        }

        NotaFiscalVenda notaFiscalVenda = notaFiscalVendaRepository.save(vendaCompraLojaVirtual.getNotaFiscalVenda());
        vendaCompraLojaVirtual.setNotaFiscalVenda(notaFiscalVenda);
        VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo = vendaCompraLojaVirtualRepository.save(vendaCompraLojaVirtual);

        StatusRastreio statusRastreio = new StatusRastreio();
        statusRastreio.setCentroDistribuicao("Loja Local");
        statusRastreio.setCidade("São Paulo");
        statusRastreio.setEmpresa(vendaCompraLojaVirtualSalvo.getEmpresa());
        statusRastreio.setStatus("Em preparação");
        statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtualSalvo);
        statusRastreioRepository.save(statusRastreio);

        notaFiscalVenda.setVendaCompraLojaVirtual(vendaCompraLojaVirtualSalvo);
        notaFiscalVendaRepository.save(notaFiscalVenda);

        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setEmpresa(vendaCompraLojaVirtualSalvo.getEmpresa());
        contaReceber.setDescricao("Venda de produto: " + vendaCompraLojaVirtualSalvo.getItensVendaLoja().get(0).getProduto().getNome());
        contaReceber.setDtPagamento(Calendar.getInstance().getTime());
        contaReceber.setDtVencimento(Calendar.getInstance().getTime());
        contaReceber.setPessoa(vendaCompraLojaVirtualSalvo.getPessoa());
        contaReceber.setStatus(StatusContaReceber.QUITADA);
        contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
        contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());

        contaReceberRepository.saveAndFlush(contaReceber);

        serviceSendEmail.enviarEmailHtml("Venda de produto: " + vendaCompraLojaVirtualSalvo.getItensVendaLoja().get(0).getProduto().getNome(), "Olá, " + vendaCompraLojaVirtualSalvo.getPessoa().getNome() + " sua compra foi realizada com sucesso, o código de rastreio é: " + vendaCompraLojaVirtualSalvo.getDataEntrega(), vendaCompraLojaVirtualSalvo.getPessoa().getEmail());
        serviceSendEmail.enviarEmailHtml("Voce vendeu o produto: " + vendaCompraLojaVirtualSalvo.getItensVendaLoja().get(0).getProduto().getNome(), "Olá, " + vendaCompraLojaVirtualSalvo.getPessoa().getNome() + " sua venda foi realizada com sucesso", vendaCompraLojaVirtualSalvo.getEmpresa().getEmail());

        return ResponseEntity.ok(vendaCompraLojaVirtualSalvo);
    }

    @GetMapping("/listarVendaDinamica/{valor}/{tipoconsulta}")
    public ResponseEntity<Iterable<VendaCompraLojaVirtual>> listarVendaDinamica(@PathVariable("valor") String valor, @PathVariable("tipoconsulta") String tipoconsulta) {

        List<VendaCompraLojaVirtual> vendasComprasLojaVirtual = null;

        if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(Long.parseLong(valor));
        }
        if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeProduto(valor.toUpperCase().trim());
        }
        if (tipoconsulta.equalsIgnoreCase("POR_CAT_PROD")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorCategoriaProduto(valor.toUpperCase().trim());
        }
        if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeCliente(valor.toUpperCase().trim());
        }
        if (tipoconsulta.equalsIgnoreCase("POR_END_COBRANCA")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEndCobranca(valor.toUpperCase().trim());
        }
        if (tipoconsulta.equalsIgnoreCase("POR_END_ENTREGA")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEndEntrega(valor.toUpperCase().trim());
        }
        if (tipoconsulta.equalsIgnoreCase("POR_CPF_PESSOA")) {
            vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorCpfPessoa(valor.toUpperCase().trim());
        }

        return ResponseEntity.ok(vendasComprasLojaVirtual);
    }

    @GetMapping("/listarVendaPorData/{dataInicio}/{dataFim}")
    public ResponseEntity<Iterable<VendaCompraLojaVirtual>> listarVendaPorData(@PathVariable String dataInicio, @PathVariable String dataFim) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date data1 = formato.parse(dataInicio);
        Date data2 = formato.parse(dataFim);
        Iterable<VendaCompraLojaVirtual> vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorData(data1, data2);
        return ResponseEntity.ok(vendasComprasLojaVirtual);
    }

    @GetMapping("/listarVendaPorProduto/{id}")
    public ResponseEntity<Iterable<VendaCompraLojaVirtual>> listarVendaPorProduto(@PathVariable Long id) {
        Iterable<VendaCompraLojaVirtual> vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(id);
        return ResponseEntity.ok(vendasComprasLojaVirtual);
    }

    @DeleteMapping("/deleteTotalBanco/{id}")
    public ResponseEntity<String> deleteTotalBanco(@PathVariable Long id) {
        if (!vendaCompraLojaVirtualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendaCompraLojaVirtualService.exclusaoTotalBanco(id);
        return ResponseEntity.ok("Venda/Compra excluída com sucesso.");
    }

    @DeleteMapping("/deleteTotalBanco2/{id}")
    public ResponseEntity<String> deleteTotalBanco2(@PathVariable Long id) {
        if (!vendaCompraLojaVirtualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendaCompraLojaVirtualService.exclusaoTotalBanco2(id);
        return ResponseEntity.ok("Venda/Compra excluída via logica com sucesso.");
    }

    @PutMapping("/ativaRegistroVendaCompraLojaVirtual/{id}")
    public ResponseEntity<String> ativaRegistroVendaCompraLojaVirtual(@PathVariable Long id) {
        if (!vendaCompraLojaVirtualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendaCompraLojaVirtualService.ativaVenda(id);
        return ResponseEntity.ok("Venda/Compra ativada com sucesso.");
    }

    @GetMapping("/listarPorIdPessoa/{id}")
    public ResponseEntity<Iterable<VendaCompraLojaVirtual>> listarPorIdPessoa(@PathVariable Long id) {
        Iterable<VendaCompraLojaVirtual> vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorIdPessoa(id);
        return ResponseEntity.ok(vendasComprasLojaVirtual);
    }

    @GetMapping("/listarVendasComprasLojaVirtual")
    public ResponseEntity<Iterable<VendaCompraLojaVirtual>> listarVendasComprasLojaVirtual() {
        Iterable<VendaCompraLojaVirtual> vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.findAllLogicamenteFalse();
        return ResponseEntity.ok(vendasComprasLojaVirtual);
    }

    @GetMapping("/listarVendaCompraLojaVirtual/{id}")
    public ResponseEntity<VendaCompraLojaVirtual> listarVendaCompraLojaVirtual(@PathVariable Long id) {
        if (vendaCompraLojaVirtualRepository.findByIdExclusao(id) == null) {
            return ResponseEntity.notFound().build();
        }
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(id);
        return ResponseEntity.ok(vendaCompraLojaVirtual);
    }

    @DeleteMapping("/excluirVendaCompraLojaVirtual/{id}")
    public ResponseEntity<String> excluirVendaCompraLojaVirtual(@PathVariable Long id) {
        if (!vendaCompraLojaVirtualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendaCompraLojaVirtualRepository.deleteById(id);
        return ResponseEntity.ok("Venda/Compra excluída com sucesso.");
    }

    @PostMapping("/consultarFrete")
    public ResponseEntity<List<EmpresaTransporteDTO>> consultaFrete(@RequestBody @Valid ConsultaFrete consultaFrete) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(consultaFrete);

        List<EmpresaTransporteDTO> retorno = new ArrayList<>();

        retorno = vendaCompraLojaVirtualService.consultarFrete(json);

        return ResponseEntity.ok(retorno);
    }

    @GetMapping("/ImprimeEtiquetaFrete/{idVenda}")
    public ResponseEntity<String> imprimeEtiquetaFrete(@PathVariable Long idVenda) throws IOException {
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElseGet(null);

        if (vendaCompraLojaVirtual == null) {
            return ResponseEntity.notFound().build();
        }

        EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();
        envioEtiquetaDTO.setService(vendaCompraLojaVirtual.getServicoTransportadora());
        envioEtiquetaDTO.getFrom().setName(vendaCompraLojaVirtual.getEmpresa().getNome());
        envioEtiquetaDTO.getFrom().setEmail(vendaCompraLojaVirtual.getEmpresa().getEmail());
        envioEtiquetaDTO.getFrom().setPhone(vendaCompraLojaVirtual.getEmpresa().getTelefone());
        envioEtiquetaDTO.getFrom().setCompany_document(vendaCompraLojaVirtual.getEmpresa().getCnpj());
        envioEtiquetaDTO.getFrom().setState_register(vendaCompraLojaVirtual.getEmpresa().getInscEstadual());
        envioEtiquetaDTO.getFrom().setAddress(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getRua());
        envioEtiquetaDTO.getFrom().setNumber(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
        envioEtiquetaDTO.getFrom().setComplement(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
        envioEtiquetaDTO.getFrom().setCity(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
        envioEtiquetaDTO.getFrom().setState_abbr(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
        envioEtiquetaDTO.getFrom().setDistrict(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getBairro());
        envioEtiquetaDTO.getFrom().setPostal_code(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
        envioEtiquetaDTO.getFrom().setCountry_id("BR");

        envioEtiquetaDTO.getTo().setName(vendaCompraLojaVirtual.getPessoa().getNome());
        envioEtiquetaDTO.getTo().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        envioEtiquetaDTO.getTo().setPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());
        envioEtiquetaDTO.getTo().setDocument(vendaCompraLojaVirtual.getPessoa().getCpf());
        envioEtiquetaDTO.getTo().setAddress(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getRua());
        envioEtiquetaDTO.getTo().setNumber(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
        envioEtiquetaDTO.getTo().setComplement(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getComplemento());
        envioEtiquetaDTO.getTo().setCity(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
        envioEtiquetaDTO.getTo().setState_abbr(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getUf());
        envioEtiquetaDTO.getTo().setDistrict(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getBairro());
        envioEtiquetaDTO.getTo().setPostal_code(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getCep());
        envioEtiquetaDTO.getTo().setCountry_id("BR");

        List<ProductsEnvioEtiqueta> productsEnvioEtiquetas = new ArrayList<>();

        for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
            ProductsEnvioEtiqueta dto = new ProductsEnvioEtiqueta();
            dto.setQuantity(itemVendaLoja.getQuantidade().toString());
            dto.setUnitary_value(itemVendaLoja.getProduto().getValorVenda().toString());
            dto.setName(itemVendaLoja.getProduto().getNome());

            productsEnvioEtiquetas.add(dto);
        }

        envioEtiquetaDTO.setProducts(productsEnvioEtiquetas);

        VolumeEnvioEtiqueta volumeEnvioEtiquetas = new VolumeEnvioEtiqueta();

        for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
            VolumeEnvioEtiqueta dto = new VolumeEnvioEtiqueta();
            volumeEnvioEtiquetas.setWeight(itemVendaLoja.getProduto().getPeso().toString());
            volumeEnvioEtiquetas.setHeight(itemVendaLoja.getProduto().getAltura().toString());
            volumeEnvioEtiquetas.setWidth(itemVendaLoja.getProduto().getLargura().toString());
            volumeEnvioEtiquetas.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
        }

        envioEtiquetaDTO.setVolumes(volumeEnvioEtiquetas);

        envioEtiquetaDTO.getOptions().setInsurance_value(vendaCompraLojaVirtual.getValorTotal().toString());
        envioEtiquetaDTO.getOptions().setReceipt(false);
        envioEtiquetaDTO.getOptions().setOwn_hand(false);
        envioEtiquetaDTO.getOptions().setNon_commercial(false);
        envioEtiquetaDTO.getOptions().setReverse(false);
        envioEtiquetaDTO.getOptions().getInvoice().setKey(vendaCompraLojaVirtual.getNotaFiscalVenda().getNumeroNota());
        envioEtiquetaDTO.getOptions().setPlatform(vendaCompraLojaVirtual.getEmpresa().getNome());
        envioEtiquetaDTO.getOptions().getTags().setTag("teste");
        envioEtiquetaDTO.getOptions().getTags().setUrl("https://www.google.com.br");

        String json = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/cart")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
        Iterator<JsonNode> iterator = jsonNode.iterator();

        String idEtiqueta = "";

        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            idEtiqueta = node.asText();
            break;
        }

        vendaCompraLojaVirtualRepository.updateEtiqueta(idEtiqueta, vendaCompraLojaVirtual.getId());


        OkHttpClient clientCompra = new OkHttpClient();

        MediaType mediaTypeCompra = MediaType.parse("application/json");
        okhttp3.RequestBody bodyCompra = okhttp3.RequestBody.create(mediaTypeCompra, "{\"orders\":[\""+idEtiqueta+"\"]}");
        Request requestCompra = new Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/checkout")
                .post(bodyCompra)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response responseCompra = clientCompra.newCall(requestCompra).execute();

        if (!responseCompra.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        OkHttpClient clientGerarEtiqueta = new OkHttpClient();

        MediaType mediaTypeGerarEtiqueta = MediaType.parse("application/json");
        okhttp3.RequestBody bodyGerarEtiqueta = okhttp3.RequestBody.create(mediaTypeGerarEtiqueta, "{\"orders\":[\""+idEtiqueta+"\"]}");
        Request requestGerarEtiqueta = new Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/generate")
                .post(bodyGerarEtiqueta)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response responseGerarEtiqueta = clientGerarEtiqueta.newCall(requestGerarEtiqueta).execute();

        if (!responseGerarEtiqueta.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        OkHttpClient clientImprimeEtiqueta = new OkHttpClient();

        MediaType mediaTypeImprimeEtiqueta = MediaType.parse("application/json");
        okhttp3.RequestBody bodyImprimeEtiqueta = okhttp3.RequestBody.create(mediaTypeImprimeEtiqueta, "{\"orders\":[\""+idEtiqueta+"\"],\"mode\":\"private\"}");
        Request requestImprimeEtiqueta = new Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/print")
                .post(bodyImprimeEtiqueta)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response responseImprimeEtiqueta = clientImprimeEtiqueta.newCall(requestImprimeEtiqueta).execute();

        if (!responseImprimeEtiqueta.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String urlEtiqueta = responseImprimeEtiqueta.body().string();

        vendaCompraLojaVirtualRepository.updateUrlEtiqueta(urlEtiqueta, vendaCompraLojaVirtual.getId());

        return ResponseEntity.ok().body("Etiqueta gerada com sucesso!");
    }
}
