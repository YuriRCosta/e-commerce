package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.login = ?1")
    Usuario findByLogin(String login);

}
