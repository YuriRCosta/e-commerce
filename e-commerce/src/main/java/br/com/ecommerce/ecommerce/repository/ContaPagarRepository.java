package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @Query("select c from ContaPagar c where lower(c.descricao) like lower(concat('%', ?1,'%'))")
    List<ContaPagar> buscarContaDesc(String desc);

    @Query("select c from ContaPagar c where c.pessoa.id = ?1")
    List<ContaPagar> buscarContaPorPessoa(Long id);

    @Query("select c from ContaPagar c where c.pessoaFornecedor.id = ?1")
    List<ContaPagar> buscarContaPorFornecedor(Long id);

    @Query("select c from ContaPagar c where c.empresa.id = ?1")
    List<ContaPagar> buscarContaPorEmpresa(Long id);
}
