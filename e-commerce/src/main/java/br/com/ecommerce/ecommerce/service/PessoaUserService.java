package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.model.Usuario;
import br.com.ecommerce.ecommerce.repository.PessoaRepository;
import br.com.ecommerce.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {
        for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
            pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
            pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
        }

        pessoaJuridica = pessoaRepository.save(pessoaJuridica);

        Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if (isUserNovo(usuarioPj)) {
            addNewUser(pessoaJuridica);
        }

        return pessoaJuridica;
    }

    public boolean isUserNovo( Usuario usuarioPj) {
        if (usuarioPj == null) {
            String constraint = usuarioRepository.consultaConstraintRole();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
                return true;
            }
            return true;
        }
        return false;
    }

    public void addNewUser(PessoaJuridica pessoaJuridica) {
        Usuario usuarioPj = new Usuario();

        usuarioPj.setLogin(pessoaJuridica.getEmail());
        usuarioPj.setEmpresa(pessoaJuridica);
        usuarioPj.setPessoa(pessoaJuridica);

        String senha = "" + Calendar.getInstance().getTimeInMillis();
        String senhaCrypt = new BCryptPasswordEncoder().encode(senha);
        usuarioPj.setSenha(senhaCrypt);

        usuarioRepository.save(usuarioPj);

        usuarioRepository.insereAcessoUSerPj(usuarioPj.getId());

        envioEmailCadastro(senha, pessoaJuridica);
    }

    public void envioEmailCadastro(String senha, PessoaJuridica pessoaJuridica) {
        StringBuilder conteudo = new StringBuilder();

        conteudo.append("<h1>Seu acesso foi gerado com sucesso!</h1>");
        conteudo.append("<p>Seu login é: " + pessoaJuridica.getEmail() + "</p>");
        conteudo.append("<p>Sua senha é: " + senha + "</p>");
        conteudo.append("<br/>");
        conteudo.append("<p>Atenciosamente, equipe E Commerce</p>");

        try {
            serviceSendEmail.enviarEmailHtml("Acesso gerado para E Commerce", conteudo.toString(), pessoaJuridica.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
