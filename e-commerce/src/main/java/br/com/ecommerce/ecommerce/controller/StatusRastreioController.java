package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.StatusRastreio;
import br.com.ecommerce.ecommerce.repository.StatusRastreioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusRastreioController {

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;

    @GetMapping("/rastrearVenda/{idVenda}")
    public ResponseEntity<StatusRastreio> rastrearVenda(@PathVariable Long idVenda) {
        return ResponseEntity.ok(statusRastreioRepository.rastrearVenda(idVenda));
    }

}
