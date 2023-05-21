package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

    @Query(value = "select a from VendaCompraLojaVirtual a where a.id = ?1 and a.excluido = false")
    VendaCompraLojaVirtual findByIdExclusao(Long id);

    @Query(value = "select a from VendaCompraLojaVirtual a where a.excluido = false")
    List<VendaCompraLojaVirtual> findAllLogicamenteFalse();

    @Query("select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.produto.id = ?1 and i.vendaCompraLojaVirtual.excluido = false")
    List<VendaCompraLojaVirtual> vendaPorProduto(Long id);

    @Query("select distinct i.vendaCompraLojaVirtual from ItemVendaLoja i where upper(trim(i.produto.nome)) like %?1% and i.vendaCompraLojaVirtual.excluido = false")
    List<VendaCompraLojaVirtual> vendaPorNomeProduto(String valor);

    @Query("select distinct i.vendaCompraLojaVirtual from ItemVendaLoja i where upper(trim(i.produto.categoriaProduto.nomeDesc)) like %?1% and i.vendaCompraLojaVirtual.excluido = false")
    List<VendaCompraLojaVirtual> vendaPorCategoriaProduto(String valor);

    @Query("select distinct i.vendaCompraLojaVirtual from ItemVendaLoja i where upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1% and i.vendaCompraLojaVirtual.excluido = false")
    List<VendaCompraLojaVirtual> vendaPorNomeCliente(String trim);

    @Query("select distinct i.vendaCompraLojaVirtual from ItemVendaLoja i where upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.rua)) like %?1% and i.vendaCompraLojaVirtual.excluido = false")
    List<VendaCompraLojaVirtual> vendaPorEndCobranca(String trim);

    @Query("select distinct i.vendaCompraLojaVirtual from ItemVendaLoja i where upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.rua)) like %?1% and i.vendaCompraLojaVirtual.excluido = false")
    List<VendaCompraLojaVirtual> vendaPorEndEntrega(String trim);
}
