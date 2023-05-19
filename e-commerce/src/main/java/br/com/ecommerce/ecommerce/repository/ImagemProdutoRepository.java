package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    @Query("SELECT i FROM ImagemProduto i WHERE i.produto.id = ?1")
    Optional<Acesso> findImagemProduto(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM imagem_produto WHERE produto_id = ?1")
    void deleteImagens(Long id);
}
