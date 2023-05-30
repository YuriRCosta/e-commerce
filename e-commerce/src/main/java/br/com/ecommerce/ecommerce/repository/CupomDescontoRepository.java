package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

    @Query(value = "select a from CupomDesconto a where a.empresa.id = ?1")
    List<CupomDesconto> findByEmpresaId(Long idEmpresa);

}
