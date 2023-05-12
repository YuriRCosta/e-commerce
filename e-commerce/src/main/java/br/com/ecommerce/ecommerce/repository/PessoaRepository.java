package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaJuridica, Long> {

    @Query("select p from PessoaJuridica p where p.cnpj = ?1")
    PessoaJuridica findByCnpj(String cnpj);

}
