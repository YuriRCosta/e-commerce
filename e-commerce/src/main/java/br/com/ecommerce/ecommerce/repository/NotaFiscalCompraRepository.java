package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.NotaFiscalCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {

    @Query("select n from NotaFiscalCompra n where lower(n.descricaoObs) like lower(concat('%', ?1,'%'))")
    List<NotaFiscalCompra> buscarNotaPorDesc(String desc);

    @Query("select n from NotaFiscalCompra n where n.pessoa.id = ?1")
    List<NotaFiscalCompra> buscarNotaPorPessoa(Long id);

    @Query("select n from NotaFiscalCompra n where n.contaPagar.id = ?1")
    List<NotaFiscalCompra> buscarNotaPorContaPagar(Long id);

    @Query("select n from NotaFiscalCompra n where n.empresa.id = ?1")
    List<NotaFiscalCompra> buscarContaPorEmpresa(Long id);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE FROM nota_item_produto WHERE nota_fiscal_compra_id = ?1")
    void deleteItemNotaFiscalCompra(Long id);
}
