package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.controller.AcessoController;
import br.com.ecommerce.ecommerce.controller.PessoaController;
import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import br.com.ecommerce.ecommerce.repository.PessoaRepository;
import br.com.ecommerce.ecommerce.service.PessoaUserService;
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
