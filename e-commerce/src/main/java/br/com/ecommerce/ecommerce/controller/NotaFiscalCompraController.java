package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.NotaFiscalCompra;
import br.com.ecommerce.ecommerce.repository.NotaFiscalCompraRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotaFiscalCompraController {

    @Autowired
    private NotaFiscalCompraRepository notaFiscalCompraRepository;

    @PostMapping("/salvarNotaFiscalCompra")
    public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionECommerce {
        if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
            throw new ExceptionECommerce("Empresa não informada.");
        }
        if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
            throw new ExceptionECommerce("Pessoa não informada.");
        }
        if (notaFiscalCompra.getId() == null) {
            List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaPorDesc(notaFiscalCompra.getDescricaoObs());
            if (notaFiscalCompras.size() > 0) {
                throw new ExceptionECommerce("Conta "+notaFiscalCompra.getDescricaoObs()+" ja cadastrada.");
            }
        }
        NotaFiscalCompra notaFiscalCompraSalvo = notaFiscalCompraRepository.save(notaFiscalCompra);
        return ResponseEntity.ok(notaFiscalCompraSalvo);
    }

    @DeleteMapping("/excluirNotaFiscalCompra/{id}")
    public ResponseEntity<String> excluirNotaFiscalCompra(@PathVariable Long id) {
        try {
            NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Categoria não encontrada.");
        }
        notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);
        notaFiscalCompraRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listarNotaFiscalCompra")
    public ResponseEntity<Iterable<NotaFiscalCompra>> listarNotaFiscalCompra() {
        Iterable<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.findAll();
        return ResponseEntity.ok(notaFiscalCompras);
    }

    @GetMapping("/listarNotaFiscalCompra/{id}")
    public ResponseEntity<NotaFiscalCompra> listarContaPagar(@PathVariable Long id) {
        if (!notaFiscalCompraRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).get();
        return ResponseEntity.ok(notaFiscalCompra);
    }

    @GetMapping("/listarNotaFiscalCompraPorDesc/{desc}")
    public ResponseEntity<Iterable<NotaFiscalCompra>> listarNotaFiscalCompraPorDesc(@PathVariable String desc) {
        Iterable<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaPorDesc(desc);
        return ResponseEntity.ok(notaFiscalCompras);
    }
}
