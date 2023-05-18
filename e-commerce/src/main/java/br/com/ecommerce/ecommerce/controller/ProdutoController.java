package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.Produto;
import br.com.ecommerce.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody Produto produto) throws ExceptionECommerce {
        if (produtoRepository.existsByNome(produto.getNome().toUpperCase())) {
            throw new ExceptionECommerce("Produto "+produto.getNome()+" ja cadastrado.");
        }
        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionECommerce("Empresa não informada.");
        }
        if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionECommerce("Categoria não informada.");
        }
        if (produto.getMarcaProduto().getId() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionECommerce("Marca não informada.");
        }
        Produto produtoSalvo = produtoRepository.save(produto);
        return ResponseEntity.ok(produtoSalvo);
    }

    @DeleteMapping("/excluirProduto/{id}")
    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
        try {
            Produto produto = produtoRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Categoria não encontrada.");
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listarProdutos")
    public ResponseEntity<Iterable<Produto>> listarProdutos() {
        Iterable<Produto> produtos = produtoRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/listarProduto/{id}")
    public ResponseEntity<Produto> listarProduto(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Produto produto = produtoRepository.findById(id).get();
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/listarProdutoPorNome/{nome}")
    public ResponseEntity<Iterable<Produto>> listarProdutoPorNome(@PathVariable String nome) {
        Iterable<Produto> produtos = produtoRepository.findByNomeList(nome);
        return ResponseEntity.ok(produtos);
    }
}
