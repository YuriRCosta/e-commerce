package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.ContaPagar;
import br.com.ecommerce.ecommerce.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContaPagarController {

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    @PostMapping("/salvarContaPagar")
    public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody ContaPagar contaPagar) throws ExceptionECommerce {
        if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
            throw new ExceptionECommerce("Empresa não informada.");
        }
        if (contaPagar.getPessoaFornecedor() == null || contaPagar.getPessoaFornecedor().getId() <= 0) {
            throw new ExceptionECommerce("Fornecedor não informada.");
        }
        if (contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0) {
            throw new ExceptionECommerce("Pessoa não informada.");
        }
        if (contaPagar.getId() == null) {
            List<ContaPagar> contasPagar = contaPagarRepository.buscarContaDesc(contaPagar.getDescricao());
            if (contasPagar.size() > 0) {
                throw new ExceptionECommerce("Conta "+contaPagar.getDescricao()+" ja cadastrada.");
            }
        }
        ContaPagar contaPagarSalvo = contaPagarRepository.save(contaPagar);
        return ResponseEntity.ok(contaPagarSalvo);
    }

    @DeleteMapping("/excluirContaPagar/{id}")
    public ResponseEntity<String> excluirContaPagar(@PathVariable Long id) {
        try {
            ContaPagar contaPagar = contaPagarRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Categoria não encontrada.");
        }
        contaPagarRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listarContasPagar")
    public ResponseEntity<Iterable<ContaPagar>> listarContasPagar() {
        Iterable<ContaPagar> contasPagar = contaPagarRepository.findAll();
        return ResponseEntity.ok(contasPagar);
    }

    @GetMapping("/listarContaPagar/{id}")
    public ResponseEntity<ContaPagar> listarContaPagar(@PathVariable Long id) {
        if (!contaPagarRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ContaPagar contaPagar = contaPagarRepository.findById(id).get();
        return ResponseEntity.ok(contaPagar);
    }

    @GetMapping("/listarContaPagarPorNome/{desc}")
    public ResponseEntity<Iterable<ContaPagar>> listarContaPagarPorNome(@PathVariable String desc) {
        Iterable<ContaPagar> contasPagar = contaPagarRepository.buscarContaDesc(desc);
        return ResponseEntity.ok(contasPagar);
    }
}
