package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.StatusRastreio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

    @Query(value = "select a from StatusRastreio a where a.vendaCompraLojaVirtual.id = ?1")
    public StatusRastreio rastrearVenda(Long idVenda);

}
