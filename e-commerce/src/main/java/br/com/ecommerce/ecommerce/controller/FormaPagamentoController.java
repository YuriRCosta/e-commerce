package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.FormaPagamento;
import br.com.ecommerce.ecommerce.repository.FormaPagamentoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @PostMapping("/salvarFormaPagamento")
    public ResponseEntity<FormaPagamento> salvarVendaCompraLojaVirtual(@RequestBody @Valid FormaPagamento formaPagamento) {
        FormaPagamento formaPagamentoSalvo = formaPagamentoRepository.save(formaPagamento);
        return ResponseEntity.ok(formaPagamentoSalvo);
    }

}
