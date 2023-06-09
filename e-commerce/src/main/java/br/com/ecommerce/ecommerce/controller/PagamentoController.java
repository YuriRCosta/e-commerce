package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import br.com.ecommerce.ecommerce.model.AccessTokenAPIPagamento;
import br.com.ecommerce.ecommerce.model.BoletoJuno;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.model.dto.*;
import br.com.ecommerce.ecommerce.repository.BoletoJunoRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import br.com.ecommerce.ecommerce.service.HostIgnoringClient;
import br.com.ecommerce.ecommerce.service.ServiceJunoBoleto;
import br.com.ecommerce.ecommerce.service.VendaCompraLojaVirtualService;
import br.com.ecommerce.ecommerce.util.ValidaCPF;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class PagamentoController {

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private VendaCompraLojaVirtualService vendaCompraLojaVirtualSer;

    @Autowired
    private ServiceJunoBoleto serviceJunoBoleto;

    @Autowired
    private BoletoJunoRepository boletoJunoRepository;

    @GetMapping("/pagamento/{id}")
    public ModelAndView pagamentoBoleto(@PathVariable(value = "id", required = false) String id){
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(Long.parseLong(id));

        ModelAndView modelAndView = new ModelAndView("pagamento");

        if (vendaCompraLojaVirtual == null) {
            modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
        }else {
            modelAndView.addObject("venda", vendaCompraLojaVirtualSer.consultaVenda(vendaCompraLojaVirtual));
        }

        return modelAndView;
    }

    @PostMapping("/finalizarCompraCartao")
    public ResponseEntity<String> finalizarCompraCartao(
            @RequestParam("cardHash") String cardHash,
            @RequestParam("cardNumber") String cardNumber,
            @RequestParam("holderName") String holderName,
            @RequestParam("securityCode") String securityCode,
            @RequestParam("expirationMonth") String expirationMonth,
            @RequestParam("expirationYear") String expirationYear,
            @RequestParam("idVendaCampo") Long idVendaCampo,
            @RequestParam("cpf") String cpf,
            @RequestParam("qtdparcela") Integer qtdparcela,
            @RequestParam("cep") String cep,
            @RequestParam("rua") String rua,
            @RequestParam("numero") String numero,
            @RequestParam("estado") String estado,
            @RequestParam("cidade") String cidade) throws Exception{

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.
                findById(idVendaCampo).orElse(null);

        if (vendaCompraLojaVirtual == null) {
            return ResponseEntity.ok("Código da venda não existe!");
        }

        String cpfLimpo =  cpf.replaceAll("\\.", "").replaceAll("\\-", "");

        if (!ValidaCPF.isCPF(cpfLimpo)) {
            return ResponseEntity.ok("CPF informado é inválido.");
        }

        if (qtdparcela > 12 || qtdparcela <= 0) {
            return ResponseEntity.ok("Quantidade de parcelar deve ser de  1 até 12.");
        }

        if (vendaCompraLojaVirtual.getValorTotal().doubleValue() <= 0) {
            return ResponseEntity.ok("Valor da venda não pode ser Zero(0).");
        }

        AccessTokenAPIPagamento accessTokenJunoAPI = serviceJunoBoleto.buscaTokenAtivo();

        if (accessTokenJunoAPI == null) {
            return ResponseEntity.ok("Autorização bancária não foi encontrada.");
        }


        CobrancaJunoApi cobrancaJunoAPI = new CobrancaJunoApi();
        cobrancaJunoAPI.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
        cobrancaJunoAPI.getCharge().setDescription("Pagamento da venda: " + vendaCompraLojaVirtual.getId() + " para o cliente: " + vendaCompraLojaVirtual.getPessoa().getNome());

        if (qtdparcela == 1) {
            cobrancaJunoAPI.getCharge().setAmount(vendaCompraLojaVirtual.getValorTotal().floatValue());
        }else {
            BigDecimal valorParcela = vendaCompraLojaVirtual.getValorTotal().divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
            cobrancaJunoAPI.getCharge().setAmount(valorParcela.floatValue());
        }

        cobrancaJunoAPI.getCharge().setInstallments(String.valueOf(qtdparcela));

        Calendar dataVencimento = Calendar.getInstance();
        dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyy-MM-dd");
        cobrancaJunoAPI.getCharge().setDueDate(dateFormater.format(dataVencimento.getTime()));

        cobrancaJunoAPI.getCharge().setFine(BigDecimal.valueOf(1.00));
        cobrancaJunoAPI.getCharge().setInterest(BigDecimal.valueOf(1.00));
        cobrancaJunoAPI.getCharge().setMaxOverdueDays(7);
        cobrancaJunoAPI.getCharge().getPaymentTypes().add("CREDIT_CARD");

        cobrancaJunoAPI.getBilling().setName(holderName);
        cobrancaJunoAPI.getBilling().setDocument(cpfLimpo);
        cobrancaJunoAPI.getBilling().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        cobrancaJunoAPI.getBilling().setPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());


        Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResource = client.resource("https://api.juno.com.br/charges");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(cobrancaJunoAPI);

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccessToken())
                .post(ClientResponse.class, json);

        String stringRetorno = clientResponse.getEntity(String.class);

        if (clientResponse.getStatus() != 200) {

            ErroResponseApiJuno jsonRetornoErro = objectMapper.
                    readValue(stringRetorno, new TypeReference<>() {});

            return ResponseEntity.ok(jsonRetornoErro.listaErro());

        }

        clientResponse.close();

        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        BoletoGeradoApiJuno jsonRetorno = objectMapper.
                readValue(stringRetorno, new TypeReference<>() { });

        int recorrencia = 1;

        List<BoletoJuno> boletosJuno = new ArrayList<>();

        for (ConteudoBoletoJuno c : jsonRetorno.get_embedded().getCharges()) {

            BoletoJuno boletoJuno = new BoletoJuno();

            boletoJuno.setChargeICartao(c.getId());
            boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
            boletoJuno.setCode(c.getCode());
            boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyy-MM-dd").parse(c.getDueDate())));
            boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            boletoJuno.setIdChrBoleto(c.getId());
            boletoJuno.setIdPix(c.getPix().getId());
            boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
            boletoJuno.setInstallmentLink(c.getInstallmentLink());
            boletoJuno.setLink(c.getLink());
            boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
            boletoJuno.setQuitado(false);
            boletoJuno.setRecorrencia(recorrencia);
            boletoJuno.setValor(new BigDecimal(c.getAmount()).setScale(2, RoundingMode.HALF_UP));
            boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

            boletoJuno = boletoJunoRepository.saveAndFlush(boletoJuno);

            boletosJuno.add(boletoJuno);
            recorrencia ++;

        }


        if (boletosJuno.isEmpty()) {
            return ResponseEntity.ok("O registro financeiro não pode ser criado para pagamento");
        }


        //------------------------REALIZANDO PAGAMENTO POR CARTÃO-------------------------

        BoletoJuno boletoJunoQuitacao = boletosJuno.get(0);

        PagamentoCartaoCredito pagamentoCartaoCredito = new PagamentoCartaoCredito();
        pagamentoCartaoCredito.setChargeId(boletoJunoQuitacao.getChargeICartao());
        pagamentoCartaoCredito.getCreditCardDetails().setCreditCardHash(cardHash);
        pagamentoCartaoCredito.getBilling().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        pagamentoCartaoCredito.getBilling().getAddress().setState(estado);
        pagamentoCartaoCredito.getBilling().getAddress().setNumber(numero);
        pagamentoCartaoCredito.getBilling().getAddress().setCity(cidade);
        pagamentoCartaoCredito.getBilling().getAddress().setStreet(rua);
        pagamentoCartaoCredito.getBilling().getAddress().setPostCode(cep.replaceAll("\\-", "").replaceAll("\\.", ""));


        Client clientCartao = new HostIgnoringCliente("https://api.juno.com.br/").hostIgnoringCliente();
        WebResource webResourceCartao = clientCartao.resource("https://api.juno.com.br/payments");

        ObjectMapper objectMapperCartao = new ObjectMapper();
        String jsonCartao = objectMapperCartao.writeValueAsString(pagamentoCartaoCredito);

        System.out.println("--------Envio dados pagamento cartão-----------: "+ jsonCartao);

        ClientResponse clientResponseCartao = webResourceCartao
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .post(ClientResponse.class, jsonCartao);

        String stringRetornoCartao = clientResponseCartao.getEntity(String.class);

        System.out.println("--------Retorno dados pagamento cartão-----------: "+ stringRetornoCartao);

        if (clientResponseCartao.getStatus() != 200) {

            ErroResponseApiJuno erroResponseApiJuno = objectMapper.
                    readValue(stringRetornoCartao, new TypeReference<ErroResponseApiJuno>() {} );

            for (BoletoJuno boletoJuno : boletosJuno) {
                serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
            }

            return new ResponseEntity<String>(erroResponseApiJuno.listaErro(), HttpStatus.OK);
        }

        RetornoPagamentoCartaoJuno retornoPagamentoCartaoJuno = objectMapperCartao.
                readValue(stringRetornoCartao, new TypeReference<RetornoPagamentoCartaoJuno>() { });

        if (retornoPagamentoCartaoJuno.getPayments().size() <= 0) {

            for (BoletoJuno boletoJuno : boletosJuno) {
                serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
            }

            return new ResponseEntity<String>("Nenhum pagamento foi retornado para processar.", HttpStatus.OK);
        }

        PaymentsCartaoCredito cartaoCredito = retornoPagamentoCartaoJuno.getPayments().get(0);

        if (!cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {
            for (BoletoJuno boletoJuno : boletosJuno) {
                serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
            }
        }

        if (cartaoCredito.getStatus().equalsIgnoreCase("DECLINED")) {
            return new ResponseEntity<String>("Pagamento rejeito pela análise de risco", HttpStatus.OK);
        }else  if (cartaoCredito.getStatus().equalsIgnoreCase("FAILED")) {
            return new ResponseEntity<String>("Pagamento não realizado por falha", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("NOT_AUTHORIZED")) {
            return new ResponseEntity<String>("Pagamento não autorizado pela instituição responsável pleo cartão de crédito, no caso, a emissora do seu cartão.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("CUSTOMER_PAID_BACK")) {
            return new ResponseEntity<String>("Pagamento estornado a pedido do cliente.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("BANK_PAID_BACK")) {
            return new ResponseEntity<String>("Pagamento estornado a pedido do banco.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("PARTIALLY_REFUNDED")) {
            return new ResponseEntity<String>("Pagamento parcialmente estornado.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {

            for (BoletoJuno boletoJuno : boletosJuno) {
                boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
            }

            vd_Cp_Loja_virt_repository.updateFinalizaVenda(vendaCompraLojaVirtual.getId());

            return new ResponseEntity<String>("sucesso", HttpStatus.OK);
        }


        return new ResponseEntity<String>("Nenhuma operação realizada!", HttpStatus.OK);

    }

}
