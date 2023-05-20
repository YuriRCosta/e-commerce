package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

}
