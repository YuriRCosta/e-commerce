package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.controller.AcessoController;
import br.com.ecommerce.ecommerce.controller.PessoaController;
import br.com.ecommerce.ecommerce.enums.TipoEndereco;
import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.model.Endereco;
import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import br.com.ecommerce.ecommerce.repository.PessoaRepository;
import br.com.ecommerce.ecommerce.service.PessoaUserService;
import br.com.ecommerce.ecommerce.service.ServiceSendEmail;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Profile("dev")
@SpringBootTest(classes = ECommerceApplication.class)
class TestePessoaUsuario {

    @Autowired
    @Mock
    private AcessoRepository acessoRepository;

    @Autowired
    private PessoaController pessoaController;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaUserService pessoaUserService;

    @Autowired
    @InjectMocks
    private AcessoController acessoController;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Test
    void testCadPessoaFisica() {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setNome("João");
        pessoaJuridica.setCnpj("123456412378910");
        pessoaJuridica.setInscEstadual("12345678910");
        pessoaJuridica.setNomeFantasia("João");
        pessoaJuridica.setInscMunicipal("12345678910");
        pessoaJuridica.setRazaoSocial("João");
        pessoaJuridica.setEmail("yurira15252323moscosta@hotmail.com");
        pessoaJuridica.setTelefone("1231233523445678910");
        pessoaJuridica.setEmpresa(pessoaJuridica);

        pessoaRepository.save(pessoaJuridica);

    }

    @Test
    void testCadPessoaJuridica() {
        // Cria um acesso existente
        PessoaJuridica pj = new PessoaJuridica();
        pj.setNome("testete123ste");
        pj.setCnpj("test123e123te123ste");
        pj.setInscEstadual("12345678910");
        pj.setNomeFantasia("João");
        pj.setInscMunicipal("12345678910");
        pj.setRazaoSocial("João");
        pj.setTipoPessoal("JURIDICA");
        pj.setEmail("teste112323tes3t12e@hotmail.com");
        pj.setTelefone("test1231233eteste12");

        Endereco endereco1 = new Endereco();
        endereco1.setCep("12345678910");
        endereco1.setCidade("testete123ste");
        endereco1.setBairro("testete123ste");
        endereco1.setUf("testete123ste");
        endereco1.setEmpresa(pj);
        endereco1.setPessoa(pj);
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setNumero("123");
        endereco1.setRua("testete123ste");

        Endereco endereco2 = new Endereco();
        endereco2.setCep("12345678910");
        endereco2.setCidade("testete123ste");
        endereco2.setBairro("testete123ste");
        endereco2.setUf("testete123ste");
        endereco2.setEmpresa(pj);
        endereco2.setPessoa(pj);
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setNumero("123");
        endereco2.setRua("testete123ste");

        pj.getEnderecos().add(endereco1);
        pj.getEnderecos().add(endereco2);

        pj = pessoaController.salvarPJ(pj).getBody();

        Assertions.assertEquals(true, pj.getId() > 0);

    }

    @Test
    void salvarPJDeveRetornarBadRequestSeAcessoExistente() {
        // Cria um acesso existente
        PessoaJuridica pjExistente = new PessoaJuridica();
        pjExistente.setNome("João");
        pjExistente.setCnpj("123");
        pjExistente.setInscEstadual("12345678910");
        pjExistente.setNomeFantasia("João");
        pjExistente.setInscMunicipal("12345678910");
        pjExistente.setRazaoSocial("João");
        pjExistente.setEmail("123@hotmail.com");
        pjExistente.setTelefone("123");
        pjExistente.setEmpresa(pjExistente);
        pessoaRepository.save(pjExistente);

        // Cria um novo acesso com a mesma descrição
        PessoaJuridica novoPJ = new PessoaJuridica();
        novoPJ.setNome("João");
        novoPJ.setCnpj("123");
        novoPJ.setInscEstadual("12345678910");
        novoPJ.setNomeFantasia("João");
        novoPJ.setInscMunicipal("12345678910");
        novoPJ.setRazaoSocial("João");
        novoPJ.setEmail("123123@hotmail.com");
        novoPJ.setTelefone("123123");
        novoPJ.setEmpresa(pjExistente);

        // Chama o método salvarAcesso do controller
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            pessoaController.salvarPJ(novoPJ);
        });

        // Verifica se a exceção contém a mensagem esperada
        Assertions.assertTrue(exception.getMessage().contains("CNPJ ja cadastrado."));
    }

    @Test
    void testSalvarAcesso() throws Exception {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();

        // Cria um objeto Acesso
        Acesso acesso = new Acesso();
        acesso.setDescricao("ROLE_ADMIN");

        ObjectMapper mapper = new ObjectMapper();

        // Envia uma requisição POST para o endpoint /salvarAcesso
        ResultActions retornoapi=  mockMvc.perform(post("/salvarAcesso")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(acesso)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));

        Acesso objetoRetorno = mapper.readValue(retornoapi.andReturn().getResponse().getContentAsString(), Acesso.class);

        Assertions.assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
    }


}
