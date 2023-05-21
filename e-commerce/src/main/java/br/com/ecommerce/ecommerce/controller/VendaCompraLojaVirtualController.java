package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.NotaFiscalVenda;
import br.com.ecommerce.ecommerce.model.StatusRastreio;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.repository.NotaFiscalVendaRepository;
import br.com.ecommerce.ecommerce.repository.StatusRastreioRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import br.com.ecommerce.ecommerce.service.VendaCompraLojaVirtualService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private StatusRastreioRepository statusRastreioRepository;

    @PostMapping("/salvarVendaCompraLojaVirtual")
    public ResponseEntity<VendaCompraLojaVirtual> salvarVendaCompraLojaVirtual(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) {

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
