package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    @Query("select p from PessoaFisica p where p.cpf = ?1")
    PessoaFisica findByCpf(String cpf);

}
