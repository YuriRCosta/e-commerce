package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.Endereco;
import br.com.ecommerce.ecommerce.model.PessoaFisica;
import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.model.Usuario;
import br.com.ecommerce.ecommerce.model.dto.CepDTO;
import br.com.ecommerce.ecommerce.model.dto.ConsultaCNPJDTO;
import br.com.ecommerce.ecommerce.repository.EnderecoRepository;
import br.com.ecommerce.ecommerce.repository.PessoaFisicaRepository;
import br.com.ecommerce.ecommerce.repository.PessoaJuridicaRepository;
import br.com.ecommerce.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {
        for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
            pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
            pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
        }

        pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);

        Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if (isUserNovo(usuarioPj)) {
            addNewUserPJ(pessoaJuridica);
        }

        return pessoaJuridica;
    }

    public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
        for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
            pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
            pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
        }

        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

        Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        if (isUserNovo(usuarioPj)) {
            addNewUserPF(pessoaFisica);
        }

        return pessoaFisica;
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

    public void addNewUserPJ(PessoaJuridica pessoaJuridica) {
        Usuario usuarioPj = new Usuario();

        usuarioPj.setLogin(pessoaJuridica.getEmail());
        usuarioPj.setEmpresa(pessoaJuridica);
        usuarioPj.setPessoa(pessoaJuridica);

        String senha = "" + Calendar.getInstance().getTimeInMillis();
        String senhaCrypt = new BCryptPasswordEncoder().encode(senha);
        usuarioPj.setSenha(senhaCrypt);

        usuarioRepository.save(usuarioPj);

        usuarioRepository.insereAcessoUSer(usuarioPj.getId());

        envioEmailCadastroPJ(senha, pessoaJuridica);
    }

    public void addNewUserPF(PessoaFisica pessoaFisica) {
        Usuario usuarioPf = new Usuario();

        usuarioPf.setLogin(pessoaFisica.getEmail());
        usuarioPf.setEmpresa(pessoaFisica);
        usuarioPf.setPessoa(pessoaFisica);

        String senha = "" + Calendar.getInstance().getTimeInMillis();
        String senhaCrypt = new BCryptPasswordEncoder().encode(senha);
        usuarioPf.setSenha(senhaCrypt);

        usuarioRepository.save(usuarioPf);

        usuarioRepository.insereAcessoUSer(usuarioPf.getId());

        envioEmailCadastroPF(senha, pessoaFisica);
    }

    public void envioEmailCadastroPJ(String senha, PessoaJuridica pessoaJuridica) {
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

    public void envioEmailCadastroPF(String senha, PessoaFisica pessoaFisica) {
        StringBuilder conteudo = new StringBuilder();

        conteudo.append("<h1>Seu acesso foi gerado com sucesso!</h1>");
        conteudo.append("<p>Seu login é: " + pessoaFisica.getEmail() + "</p>");
        conteudo.append("<p>Sua senha é: " + senha + "</p>");
        conteudo.append("<br/>");
        conteudo.append("<p>Atenciosamente, equipe E Commerce</p>");

        try {
            serviceSendEmail.enviarEmailHtml("Acesso gerado para E Commerce", conteudo.toString(), pessoaFisica.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CepDTO consultaCep(String cep) {
        return new RestTemplate().getForEntity("https://viacep.com.br/ws/" + cep + "/json/", CepDTO.class).getBody();
    }

    public ConsultaCNPJDTO consultaCnpjWS(String cnpj) {
        return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/" + cnpj, ConsultaCNPJDTO.class).getBody();
    }

    public void enderecoApiPJ(PessoaJuridica pessoaJuridica) {
        if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
            for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
                CepDTO cepDTO = consultaCep(pessoaJuridica.getEnderecos().get(i).getCep());

                pessoaJuridica.getEnderecos().get(i).setCidade(cepDTO.getLocalidade());
                pessoaJuridica.getEnderecos().get(i).setUf(cepDTO.getUf());
            }
        } else {
            for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
                Endereco enderecoTemporario = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(i).getId()).get();

                if (!enderecoTemporario.getCep().equals(pessoaJuridica.getEnderecos().get(i).getCep())) {
                    CepDTO cepDTO = consultaCep(pessoaJuridica.getEnderecos().get(i).getCep());

                    pessoaJuridica.getEnderecos().get(i).setCidade(cepDTO.getLocalidade());
                    pessoaJuridica.getEnderecos().get(i).setUf(cepDTO.getUf());

                }
            }
        }
    }

    public void enderecoApiPF(PessoaFisica pessoaFisica) {
        if (pessoaFisica.getId() == null || pessoaFisica.getId() <= 0) {
            for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
                CepDTO cepDTO = consultaCep(pessoaFisica.getEnderecos().get(i).getCep());

                pessoaFisica.getEnderecos().get(i).setCidade(cepDTO.getLocalidade());
                pessoaFisica.getEnderecos().get(i).setUf(cepDTO.getUf());
            }
        } else {
            for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
                Endereco enderecoTemporario = enderecoRepository.findById(pessoaFisica.getEnderecos().get(i).getId()).get();

                if (!enderecoTemporario.getCep().equals(pessoaFisica.getEnderecos().get(i).getCep())) {
                    CepDTO cepDTO = consultaCep(pessoaFisica.getEnderecos().get(i).getCep());

                    pessoaFisica.getEnderecos().get(i).setCidade(cepDTO.getLocalidade());
                    pessoaFisica.getEnderecos().get(i).setUf(cepDTO.getUf());

                }
            }
        }
    }
}
