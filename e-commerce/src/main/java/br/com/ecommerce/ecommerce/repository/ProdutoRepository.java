package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(value = "SELECT COUNT(1) > 0 FROM produto WHERE upper(nome) = ?1", nativeQuery = true)
    boolean existsByNome(String nome);

    @Query("select p from Produto p where lower(p.nome) like lower(concat('%', ?1,'%'))")
    List<Produto> findByNomeList(String nome);
}
