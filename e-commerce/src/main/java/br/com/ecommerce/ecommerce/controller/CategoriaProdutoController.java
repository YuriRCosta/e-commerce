package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.CategoriaProduto;
import br.com.ecommerce.ecommerce.model.dto.CategoriaProdutoDTO;
import br.com.ecommerce.ecommerce.repository.CategoriaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @PostMapping("/salvarCategoriaProduto")
    public ResponseEntity<CategoriaProdutoDTO> salvarCategoriaProduto(@RequestBody CategoriaProduto categoriaProduto) throws Exception {
        if (categoriaProduto.getNomeDesc() == null || categoriaProduto.getEmpresa().getId() == null) {
            throw new ExceptionECommerce("Nome da categoria e ID da empresa não podem ser nulos.");
        }
        if (categoriaProdutoRepository.existsByNomeDesc(categoriaProduto.getNomeDesc().toUpperCase())) {
            throw new ExceptionECommerce("Categoria "+categoriaProduto.getNomeDesc()+" ja cadastrada.");
        }

        categoriaProduto = categoriaProdutoRepository.save(categoriaProduto);
        CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
        categoriaProdutoDTO.setId(categoriaProduto.getId());
        categoriaProdutoDTO.setNomeDesc(categoriaProduto.getNomeDesc());
        categoriaProdutoDTO.setEmpresa(categoriaProduto.getEmpresa().getId().toString());
        return ResponseEntity.ok(categoriaProdutoDTO);
    }

    @DeleteMapping("/deletarCategoriaProduto/{id}")
    public ResponseEntity<String> deletarCategoriaProduto(@PathVariable Long id) {
        try {
            CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Categoria não encontrada.");
        }
        categoriaProdutoRepository.deleteById(id);
        return ResponseEntity.ok("Categoria deletada com sucesso.");
    }

    @GetMapping("/listarCategoriaProduto")
    public ResponseEntity<Iterable<CategoriaProduto>> listarCategoriaProduto() {
        Iterable<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.findAll();
        return ResponseEntity.ok(categoriaProduto);
    }

    @GetMapping("/listarCategoriaProduto/{id}")
    public ResponseEntity<CategoriaProduto> listarCategoriaProduto(@PathVariable Long id) {
        if (!categoriaProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).get();
        return ResponseEntity.ok(categoriaProduto);
    }

    @GetMapping("/listarCategoriaProdutoPorNome/{nomeDesc}")
    public ResponseEntity<List<CategoriaProduto>> listarCategoriaProdutoPorNome(@PathVariable String nomeDesc) {
        List<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.findByNomeList(nomeDesc);
        return ResponseEntity.ok(categoriaProduto);
    }
}
