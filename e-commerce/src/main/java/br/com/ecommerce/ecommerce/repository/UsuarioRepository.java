package br.com.ecommerce.ecommerce.repository;

import br.com.ecommerce.ecommerce.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.login = ?1")
    Usuario findByLogin(String login);

    @Query("select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
    Usuario findUserByPessoa(Long id, String email);

    @Query(nativeQuery = true, value = "select constraint_name from information_schema.constraint_column_usage where table_name = 'usuarios_acesso' and column_name = 'acesso_id' and constraint_name <> 'unique_acesso_usuario'")
    String consultaConstraintRole();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into usuarios_acesso (usuario_id, acesso_id) values (?1, (select id from acesso where descricao = 'ROLE_USER'))")
    void insereAcessoUSer(Long id);

    /*@Query(value = "select u from Usuario u where u.dataAtualSenha <= current_date - 90")
    List<Usuario> usuarioSenhaVencida();*/

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into usuarios_acesso (usuario_id, acesso_id) values (?1, (select id from acesso where descricao = ?2))")
    void insereAcessoUSerPj(Long id, String acesso);
}
