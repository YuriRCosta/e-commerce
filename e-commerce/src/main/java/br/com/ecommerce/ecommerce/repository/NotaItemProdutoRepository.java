package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.NotaItemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

    @Query("select n from NotaItemProduto n where n.produto.id = ?1 and n.notaFiscalCompra.id = ?2")
    List<NotaItemProduto> buscarNotaItemPorProdutoNota(Long idProduto, Long idNota);

    @Query("select n from NotaItemProduto n where n.produto.id = ?1")
    List<NotaItemProduto> buscarNotaItemPorProduto(Long id);

    @Query("select n from NotaItemProduto n where n.notaFiscalCompra.id = ?1")
    List<NotaItemProduto> buscarNotaItemPorNotaFiscal(Long id);

    @Query("select n from NotaItemProduto n where n.empresa.id = ?1")
    List<NotaItemProduto> buscarNotaItemPorEmpresa(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from nota_item_produto where id = ?1")
    void deleteByIdNotaItem(Long id);
}
