package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.AvaliacaoProduto;
import br.com.ecommerce.ecommerce.model.dto.AvaliacaoProdutoDTO;
import br.com.ecommerce.ecommerce.repository.AvaliacaoProdutoRepository;
import br.com.ecommerce.ecommerce.service.AvaliacaoProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AvaliacaoProdutoController {

    @Autowired
    private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

    @Autowired
    private AvaliacaoProdutoService avaliacaoProdutoService;

    @PostMapping("/salvarAvaliacaoProduto")
    public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionECommerce {
        if (avaliacaoProduto.getNota() < 1 || avaliacaoProduto.getNota() > 10) {
            throw new ExceptionECommerce("A nota deve ser entre 0 e 10");
        }
        AvaliacaoProduto avaliacaoProdutoSalvo = avaliacaoProdutoRepository.save(avaliacaoProduto);
        return ResponseEntity.ok(avaliacaoProdutoSalvo);
    }

    @GetMapping("/listarAvaliacaoProdutos")
    public ResponseEntity<List<AvaliacaoProdutoDTO>> listarAvaliacaoProdutos() {
        List<AvaliacaoProduto> avaliacoesProdutos = avaliacaoProdutoRepository.findAll();

        List<AvaliacaoProdutoDTO> avaliacoesProdutosDTO = new ArrayList<>();

        for (AvaliacaoProduto avaliacaoProduto : avaliacoesProdutos) {
            AvaliacaoProdutoDTO avaliacaoProdutoDTO = avaliacaoProdutoService.montarDTO(avaliacaoProduto);

            avaliacoesProdutosDTO.add(avaliacaoProdutoDTO);
        }

        return ResponseEntity.ok(avaliacoesProdutosDTO);
    }

    @GetMapping("/listarAvaliacaoProduto/{id}")
    public ResponseEntity<AvaliacaoProdutoDTO> listarAvaliacaoProduto(@PathVariable Long id) {
        if (!avaliacaoProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        AvaliacaoProduto avaliacaoProduto = avaliacaoProdutoRepository.findById(id).get();

        AvaliacaoProdutoDTO avaliacaoProdutoDTO = new AvaliacaoProdutoDTO();

        avaliacaoProdutoDTO = avaliacaoProdutoService.montarDTO(avaliacaoProduto);

        return ResponseEntity.ok(avaliacaoProdutoDTO);
    }

    @DeleteMapping("/deletarAvaliacaoProduto/{id}")
    public ResponseEntity<Void> deletarAvaliacaoProduto(@PathVariable Long id) {
        if (!avaliacaoProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        avaliacaoProdutoRepository.deleteAvaliacaoProdutoByID(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/avaliacaoProdutoPorPessoa/{id}/{idPessoa}")
    public ResponseEntity<AvaliacaoProdutoDTO> avaliacaoProdutoPorPessoa(@PathVariable Long id, @PathVariable Long idPessoa) {
        if (!avaliacaoProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        AvaliacaoProduto avaliacaoProduto = avaliacaoProdutoRepository.findById(id).get();

        if (avaliacaoProduto.getPessoa().getId() != idPessoa) {
            return ResponseEntity.notFound().build();
        }

        AvaliacaoProdutoDTO avaliacaoProdutoDTO = new AvaliacaoProdutoDTO();

        avaliacaoProdutoDTO = avaliacaoProdutoService.montarDTO(avaliacaoProduto);

        return ResponseEntity.ok(avaliacaoProdutoDTO);
    }
}
