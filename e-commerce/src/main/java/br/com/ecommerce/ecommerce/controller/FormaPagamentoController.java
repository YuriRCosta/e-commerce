package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.FormaPagamento;
import br.com.ecommerce.ecommerce.repository.FormaPagamentoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @PostMapping("/salvarFormaPagamento")
    public ResponseEntity<FormaPagamento> salvarVendaCompraLojaVirtual(@RequestBody @Valid FormaPagamento formaPagamento) {
        FormaPagamento formaPagamentoSalvo = formaPagamentoRepository.save(formaPagamento);
        return ResponseEntity.ok(formaPagamentoSalvo);
    }

    @GetMapping("/listarFormasPagamento")
    public ResponseEntity<List<FormaPagamento>> listarFormasPagamento() {
        return ResponseEntity.ok(formaPagamentoRepository.findAll());
    }

    @GetMapping("/listarFormaPagamentoPorId/{codigoFormaPagamento}")
    public ResponseEntity<FormaPagamento> listarFormaPagamentoPorId(@PathVariable Long codigoFormaPagamento) {
        if (formaPagamentoRepository.findById(codigoFormaPagamento).isPresent())
            return ResponseEntity.ok(formaPagamentoRepository.findById(codigoFormaPagamento).get());
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/listarFormaPagamentoPorEmpresa/{empresaId}")
    public ResponseEntity<List<FormaPagamento>> listarFormaPagamentoPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(formaPagamentoRepository.findByEmpresaId(empresaId));
    }

}
