package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.NotaFiscalVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

    @Query(value = "select a from NotaFiscalVenda a where a.vendaCompraLojaVirtual.id = ?1")
    List<NotaFiscalVenda> findByVendaCompraLojaVirtualId(Long idVenda);

    @Query(value = "select a from NotaFiscalVenda a where a.vendaCompraLojaVirtual.id = ?1")
    NotaFiscalVenda findByVendaId(Long idVenda);



}
