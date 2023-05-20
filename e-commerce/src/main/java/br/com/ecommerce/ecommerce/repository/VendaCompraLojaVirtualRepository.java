package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {



}
