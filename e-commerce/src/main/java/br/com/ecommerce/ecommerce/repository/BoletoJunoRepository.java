package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.BoletoJuno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoletoJunoRepository extends JpaRepository<BoletoJuno, Long> {

    @Query(value = "select b from BoletoJuno b where b.code = ?1")
    public BoletoJuno findByCodigoBoletoPix(String codigoBoletoPix);

    @Query(value = "select b from BoletoJuno b where b.id = ?1 and b.quitado = false")
    public List<BoletoJuno> cobrancaVendaCompra(Long codigoVendaCompra);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "update boleto_juno set quitado = true where code = ?1")
    public void quitarBoletoJuno(Long codigoBoletoPix);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "update boleto_juno set quitado = true where id = ?1")
    public void quitarBoletoById(Long id);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "delete from boleto_juno where code = ?1")
    public void deleteByCode(String code);
}