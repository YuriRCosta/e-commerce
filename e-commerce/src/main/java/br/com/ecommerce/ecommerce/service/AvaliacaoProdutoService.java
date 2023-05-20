package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.AvaliacaoProduto;
import br.com.ecommerce.ecommerce.model.dto.AvaliacaoProdutoDTO;
import br.com.ecommerce.ecommerce.repository.AvaliacaoProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoProdutoService {

    @Autowired
    private AvaliacaoProdutoRepository avaliacaoProdutoRepository;

    public AvaliacaoProdutoDTO montarDTO(AvaliacaoProduto avaliacaoProduto) {
        AvaliacaoProdutoDTO avaliacaoProdutoDTO = new AvaliacaoProdutoDTO();
        avaliacaoProdutoDTO.setId(avaliacaoProduto.getId());
        avaliacaoProdutoDTO.setNota(avaliacaoProduto.getNota());
        avaliacaoProdutoDTO.setEmpresa(avaliacaoProduto.getEmpresa().getId());
        avaliacaoProdutoDTO.setProduto(avaliacaoProduto.getProduto().getId());
        avaliacaoProdutoDTO.setPessoa(avaliacaoProduto.getPessoa().getId());

        if (avaliacaoProduto.getDescricao() != null) {
            avaliacaoProdutoDTO.setDescricao(avaliacaoProduto.getDescricao());
        }

        return avaliacaoProdutoDTO;
    }

}
