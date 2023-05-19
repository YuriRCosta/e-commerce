package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.Produto;
import br.com.ecommerce.ecommerce.repository.ProdutoRepository;
import br.com.ecommerce.ecommerce.service.ProdutoService;
import br.com.ecommerce.ecommerce.service.ServiceSendEmail;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @PostMapping("/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionECommerce, MessagingException, IOException {
        produtoService.verificaIfs(produto);
        produtoService.verificaImagem(produto);
        produtoService.salvarImagem(produto);

        Produto produtoSalvo = produtoRepository.save(produto);
        produtoService.alertaQtdEstoque(produtoSalvo);
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
