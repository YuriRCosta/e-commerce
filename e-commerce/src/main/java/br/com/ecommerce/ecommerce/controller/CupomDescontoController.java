package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.CupomDesconto;
import br.com.ecommerce.ecommerce.repository.CupomDescontoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CupomDescontoController {

    @Autowired
    private CupomDescontoRepository cupomDescontoRepository;

    @PostMapping("/salvarCupomDesconto")
    public ResponseEntity<CupomDesconto> salvarCupomDesconto(@RequestBody @Valid CupomDesconto cupomDesconto) {
        CupomDesconto cupomDescontoSalvo = cupomDescontoRepository.save(cupomDesconto);
        return ResponseEntity.ok(cupomDescontoSalvo);
    }

    @GetMapping("/listarCuponsDesconto")
    public ResponseEntity<Iterable<CupomDesconto>> listarCuponsDesconto() {
        return ResponseEntity.ok(cupomDescontoRepository.findAll());
    }

    @GetMapping("/listarCupomDescontoPorId/{codigoCupomDesconto}")
    public ResponseEntity<CupomDesconto> listarCupomDescontoPorId(@PathVariable Long id) {
        if (cupomDescontoRepository.findById(id).isPresent())
            return ResponseEntity.ok(cupomDescontoRepository.findById(id).get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deletarCupomDesconto/{codigoCupomDesconto}")
    public ResponseEntity<CupomDesconto> deletarCupomDesconto(@PathVariable Long id) {
        if (cupomDescontoRepository.findById(id).isPresent()) {
            cupomDescontoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
