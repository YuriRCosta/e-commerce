package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.AvaliacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

    @Query("SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = ?1")
    List<AvaliacaoProduto> findByAvaliacaoId(Long id);

    @Query("SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = ?1 AND a.pessoa.id = ?2")
    List<AvaliacaoProduto> findByAvaliacaoPessoa(Long id, Long idPessoa);

    @Query("SELECT a FROM AvaliacaoProduto a WHERE a.pessoa.id = ?1")
    List<AvaliacaoProduto> avaliacaoPessoa(Long idPessoa);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM avaliacao_produto WHERE avaliacao_produto.id = ?1")
    void deleteAvaliacaoProdutoByID (Long id);
}
