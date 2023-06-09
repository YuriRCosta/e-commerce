package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import br.com.ecommerce.ecommerce.service.AcessoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class AcessoController {

    @Autowired
    private AcessoService acessoService;
    @Autowired
    private AcessoRepository acessoRepository;

    @PostMapping("/salvarAcesso")
    public ResponseEntity<Acesso> salvarAcesso(@RequestBody @Valid Acesso acesso) {
        Optional<Acesso> acessoExistente = acessoRepository.findByDescricao(acesso.getDescricao());
        if (acessoExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um Acesso com a mesma descrição.");
        }
        Acesso acessoSalvo = acessoService.save(acesso);
        return ResponseEntity.ok(acessoSalvo);
    }

    @DeleteMapping("/excluirAcesso/{id}")
    public ResponseEntity<String> excluirAcesso(@PathVariable Long id) {
        try {
            Acesso acesso = acessoRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Produto não encontrada.");
        }
        acessoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
