package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.NotaFiscalVenda;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.repository.EnderecoRepository;
import br.com.ecommerce.ecommerce.repository.NotaFiscalVendaRepository;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        NotaFiscalVenda notaFiscalVenda = notaFiscalVendaRepository.save(vendaCompraLojaVirtual.getNotaFiscalVenda());
        vendaCompraLojaVirtual.setNotaFiscalVenda(notaFiscalVenda);
        VendaCompraLojaVirtual vendaCompraLojaVirtualSalvo = vendaCompraLojaVirtualRepository.save(vendaCompraLojaVirtual);

        notaFiscalVenda.setVendaCompraLojaVirtual(vendaCompraLojaVirtualSalvo);
        notaFiscalVendaRepository.save(notaFiscalVenda);

        return ResponseEntity.ok(vendaCompraLojaVirtualSalvo);
    }



}
