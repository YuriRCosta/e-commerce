package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query(value = "SELECT COUNT(1) > 0 FROM categoria_produto WHERE upper(nome_desc) = ?1", nativeQuery = true)
    boolean existsByNomeDesc(String nomeDesc);

    @Query("select c from CategoriaProduto c where lower(c.nomeDesc) like lower(concat('%', ?1,'%'))")
    List<CategoriaProduto> findByNomeList(String nomeDesc);
}
