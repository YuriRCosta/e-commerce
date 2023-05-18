package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.MarcaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

    @Query(value = "SELECT COUNT(1) > 0 FROM marca_produto WHERE upper(nome_desc) = ?1", nativeQuery = true)
    boolean existsByNome(String nome);

    @Query("select mp from MarcaProduto mp where lower(mp.nomeDesc) like lower(concat('%', ?1,'%'))")
    List<MarcaProduto> findByNomeList(String nome);
}
