package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.NotaFiscalVenda;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.repository.EnderecoRepository;
import br.com.ecommerce.ecommerce.repository.NotaFiscalVendaRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VendaCompraLojaVirtualController {

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping("/salvarVendaCompraLojaVirtual")
    public ResponseEntity<VendaCompraLojaVirtual> salvarVendaCompraLojaVirtual(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) {

        for (int i = 0; i < vendaCompraLojaVirtual.getItensVendaLoja().size(); i++) {
            vendaCompraLojaVirtual.getItensVendaLoja().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItensVendaLoja().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        }

        NotaFiscalVenda notaFiscalVenda = notaFiscalVendaRepository.save(vendaCompraLojaVirtual.getNotaFiscalVenda());
        vendaCompraLojaVirtual.setNotaFiscalVenda(notaFiscalVenda);
        VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo = vendaCompraLojaVirtualRepository.save(vendaCompraLojaVirtual);

        notaFiscalVenda.setVendaCompraLojaVirtual(vendaCompraLojaVirtualSalvo);
        notaFiscalVendaRepository.save(notaFiscalVenda);

        return ResponseEntity.ok(vendaCompraLojaVirtualSalvo);
    }

    @GetMapping("/listarVendasComprasLojaVirtual")
    public ResponseEntity<Iterable<VendaCompraLojaVirtual>> listarVendasComprasLojaVirtual() {
        Iterable<VendaCompraLojaVirtual> vendasComprasLojaVirtual = vendaCompraLojaVirtualRepository.findAll();
        return ResponseEntity.ok(vendasComprasLojaVirtual);
    }

    @GetMapping("/listarVendaCompraLojaVirtual/{id}")
    public ResponseEntity<VendaCompraLojaVirtual> listarVendaCompraLojaVirtual(@PathVariable Long id) {
        if (!vendaCompraLojaVirtualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(id).get();
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
