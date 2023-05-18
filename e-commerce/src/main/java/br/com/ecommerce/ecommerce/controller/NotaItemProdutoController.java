package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.NotaItemProduto;
import br.com.ecommerce.ecommerce.repository.NotaItemProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotaItemProdutoController {

    @Autowired
    private NotaItemProdutoRepository notaItemProdutoRepository;

    @PostMapping("/salvarNotaItemProduto")
    public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionECommerce {
        if (notaItemProduto.getId() == null) {
            if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {
                throw new ExceptionECommerce("Produto não informado.");
            }
            if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0) {
                throw new ExceptionECommerce("Nota não informada.");
            }
            if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {
                throw new ExceptionECommerce("Empresa não informada.");
            }
            List<NotaItemProduto> notaExistente = notaItemProdutoRepository.buscarNotaItemPorProdutoNota(notaItemProduto.getProduto().getId(), notaItemProduto.getNotaFiscalCompra().getId());
            if (!notaExistente.isEmpty()) {
                throw new ExceptionECommerce("Nota ja cadastrada.");
            }
        }

        NotaItemProduto notaItemProdutoSalvo = notaItemProdutoRepository.save(notaItemProduto);
        return ResponseEntity.ok(notaItemProdutoSalvo);
    }

    @DeleteMapping("/excluirNotaItemProduto/{id}")
    public ResponseEntity<String> excluirNotaItemProduto(@PathVariable Long id) {
        try {
            NotaItemProduto notaItemProduto = notaItemProdutoRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Categoria não encontrada.");
        }
        notaItemProdutoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listarNotaItemProduto")
    public ResponseEntity<Iterable<NotaItemProduto>> listarNotaItemProduto() {
        Iterable<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository.findAll();
        return ResponseEntity.ok(notaItemProdutos);
    }

    @GetMapping("/listarNotaItemProduto/{id}")
    public ResponseEntity<NotaItemProduto> listarNotaItemProduto(@PathVariable Long id) {
        if (!notaItemProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        NotaItemProduto notaFiscalCompra = notaItemProdutoRepository.findById(id).get();
        return ResponseEntity.ok(notaFiscalCompra);
    }

}
