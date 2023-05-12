package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AcessoRepository extends JpaRepository<Acesso, Long> {

    @Query("select a from Acesso a where upper(trim(a.descricao)) = upper(?1)")
    Optional<Acesso> findByDescricao(String descricao);

}
