package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.AccessTokenAPIPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenJunoRepository extends JpaRepository<AccessTokenAPIPagamento, Long> {



}
