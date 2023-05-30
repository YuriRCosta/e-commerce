package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

    @Query(value = "select a from FormaPagamento a where a.empresa.id = ?1")
    List<FormaPagamento> findByEmpresaId(Long idEmpresa);

}
