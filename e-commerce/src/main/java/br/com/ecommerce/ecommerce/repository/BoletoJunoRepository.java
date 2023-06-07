package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.BoletoJuno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BoletoJunoRepository extends JpaRepository<BoletoJuno, Long> {

    @Query(value = "select b from BoletoJuno b where b.code = ?1")
    public BoletoJuno findByCodigoBoletoPix(String codigoBoletoPix);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "update boleto_juno set quitado = true where code = ?1")
    public void quitarBoletoJuno(Long codigoBoletoPix);

}