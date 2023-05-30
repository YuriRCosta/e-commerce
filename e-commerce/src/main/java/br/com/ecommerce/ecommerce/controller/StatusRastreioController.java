package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.StatusRastreio;
import br.com.ecommerce.ecommerce.repository.StatusRastreioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusRastreioController {

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;

    @GetMapping("/listarStatusRastreio/{idVenda}")
    public ResponseEntity<List<StatusRastreio>> listarStatusRastreio(@PathVariable Long idVenda) {
        return ResponseEntity.ok(statusRastreioRepository.listaRastreioVenda(idVenda));
    }

}
