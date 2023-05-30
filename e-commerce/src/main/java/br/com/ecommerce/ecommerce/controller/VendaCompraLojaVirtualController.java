package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.enums.StatusContaReceber;
import br.com.ecommerce.ecommerce.model.ContaReceber;
import br.com.ecommerce.ecommerce.model.NotaFiscalVenda;
import br.com.ecommerce.ecommerce.model.StatusRastreio;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.repository.ContaReceberRepository;
import br.com.ecommerce.ecommerce.repository.NotaFiscalVendaRepository;
import br.com.ecommerce.ecommerce.repository.StatusRastreioRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import br.com.ecommerce.ecommerce.service.ServiceSendEmail;
import br.com.ecommerce.ecommerce.service.VendaCompraLojaVirtualService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


}
