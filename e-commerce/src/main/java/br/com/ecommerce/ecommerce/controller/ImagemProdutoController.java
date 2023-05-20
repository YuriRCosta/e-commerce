package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.ImagemProduto;
import br.com.ecommerce.ecommerce.model.dto.ImagemProdutoDTO;
import br.com.ecommerce.ecommerce.repository.ImagemProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagemProdutoController {

    @Autowired
    private ImagemProdutoRepository imagemProdutoRepository;

    @PostMapping("/salvarImagemProduto")
    public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody @Valid ImagemProduto imagemProduto) {
        ImagemProduto imagemProdutoSalvo = imagemProdutoRepository.save(imagemProduto);

        ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
        imagemProdutoDTO.setId(imagemProdutoSalvo.getId());
        imagemProdutoDTO.setImagemMiniatura(imagemProdutoSalvo.getImagemMiniatura());
        imagemProdutoDTO.setImagemOriginal(imagemProdutoSalvo.getImagemOriginal());
        imagemProdutoDTO.setProduto(imagemProdutoSalvo.getProduto().getId());
        imagemProdutoDTO.setEmpresa(imagemProdutoSalvo.getEmpresa().getId());

        return ResponseEntity.ok(imagemProdutoDTO);
    }

    @GetMapping("/listarImagensProdutos")
    public ResponseEntity<List<ImagemProdutoDTO>> listarImagensProdutos() {
        List<ImagemProduto> imagensProdutos = imagemProdutoRepository.findAll();
        List<ImagemProdutoDTO> imagensProdutosDTO = new ArrayList<>();

        for (ImagemProduto imagemProduto : imagensProdutos) {
            ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
            imagemProdutoDTO.setId(imagemProduto.getId());
            imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
            imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
            imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
            imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());

            imagensProdutosDTO.add(imagemProdutoDTO);
        }

        return ResponseEntity.ok(imagensProdutosDTO);
    }

    @GetMapping("/listarImagemProduto/{id}")
    public ResponseEntity<ImagemProdutoDTO> listarImagemProduto(@PathVariable Long id) {
        if (!imagemProdutoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ImagemProduto imagemProduto = imagemProdutoRepository.findById(id).get();

        ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
        imagemProdutoDTO.setId(imagemProduto.getId());
        imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
        imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
        imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
        imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());

        return ResponseEntity.ok(imagemProdutoDTO);
    }

    @DeleteMapping("/excluirImagemProduto/{id}")
    public ResponseEntity<String> excluirImagemProduto(@PathVariable Long id) {
        try {
            ImagemProduto imagemProduto = imagemProdutoRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.ok("Imagem do produto não encontrada.");
        }
        imagemProdutoRepository.deleteImagens(id);
        return ResponseEntity.noContent().build();
    }
}
