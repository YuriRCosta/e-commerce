package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.controller.AcessoController;
import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import br.com.ecommerce.ecommerce.service.AcessoService;
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
class ECommerceApplicationTests {

	@Autowired
	@Mock
	private AcessoRepository acessoRepository;

	@Autowired
	@Mock
	private AcessoService acessoService;

	@Autowired
	@InjectMocks
	private AcessoController acessoController;

	@Autowired
	private WebApplicationContext wac;

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

	@Test
	void salvarAcessoDeveRetornarBadRequestSeAcessoExistente() {
		// Cria um acesso existente
		Acesso acessoExistente = new Acesso();
		acessoExistente.setDescricao("Acesso existente");
		acessoRepository.save(acessoExistente);

		// Cria um novo acesso com a mesma descrição
		Acesso novoAcesso = new Acesso();
		novoAcesso.setDescricao("Acesso existente");

		// Chama o método salvarAcesso do controller
		ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
			acessoController.salvarAcesso(novoAcesso);
		});

		// Verifica se a exceção contém a mensagem esperada
		Assertions.assertTrue(exception.getMessage().contains("Já existe um Acesso com a mesma descrição."));
	}

	@Test
	void contextLoads() {
	}

}
