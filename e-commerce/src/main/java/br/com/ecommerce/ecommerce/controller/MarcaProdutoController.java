package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.MarcaProduto;
import br.com.ecommerce.ecommerce.repository.MarcaProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MarcaProdutoController {

    @Autowired
    private MarcaProdutoRepository marcaProdutoRepository;

    @PostMapping("/salvarMarcaProduto")
    public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionECommerce {
        if (marcaProdutoRepository.existsByNome(marcaProduto.getNomeDesc().toUpperCase())) {
            throw new ExceptionECommerce("Produto "+marcaProduto.getNomeDesc()+" ja cadastrado.");
        }
        if (marcaProduto.getEmpresa() == null || marcaProduto.getEmpresa().getId() <= 0) {
            throw new ExceptionECommerce("Empresa não informada.");
        }
        MarcaProduto marcaProdutoSalvo = marcaProdutoRepository.save(marcaProduto);
        return ResponseEntity.ok(marcaProdutoSalvo);
    }

    @DeleteMapping("/excluirMarcaProduto/{id}")
    public ResponseEntity<String> excluirMarcaProduto(@PathVariable Long id) {
        try {
            MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Categoria não encontrada.");
        }
        marcaProdutoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listarMarcaProdutos")
    public ResponseEntity<Iterable<MarcaProduto>> listarMarcaProdutos() {
        Iterable<MarcaProduto> marcaProdutos = marcaProdutoRepository.findAll();
        return ResponseEntity.ok(marcaProdutos);
    }

    @GetMapping("/listarMarcaProduto/{id}")
    public ResponseEntity<MarcaProduto> listarMarcaProduto(@PathVariable Long id) {
        if (!marcaProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).get();
        return ResponseEntity.ok(marcaProduto);
    }

    @GetMapping("/listarMarcaProdutoPorNome/{nome}")
    public ResponseEntity<Iterable<MarcaProduto>> listarProdutoPorNome(@PathVariable String nome) {
        Iterable<MarcaProduto> marcaProdutos = marcaProdutoRepository.findByNomeList(nome);
        return ResponseEntity.ok(marcaProdutos);
    }
}
