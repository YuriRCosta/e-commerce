package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import br.com.ecommerce.ecommerce.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AcessoController {

    @Autowired
    private AcessoService acessoService;
    @Autowired
    private AcessoRepository acessoRepository;

    @PostMapping("/salvarAcesso")
    public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) {
        Acesso acessoSalvo = acessoService.save(acesso);
        return ResponseEntity.ok(acessoSalvo);
    }

    @DeleteMapping("/excluirAcesso/{id}")
    public ResponseEntity<Void> excluirAcesso(@PathVariable Long id) {
        acessoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
