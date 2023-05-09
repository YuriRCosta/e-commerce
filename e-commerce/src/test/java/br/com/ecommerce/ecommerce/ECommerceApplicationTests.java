package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.controller.AcessoController;
import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import br.com.ecommerce.ecommerce.service.AcessoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ECommerceApplication.class)
public class ECommerceApplicationTests {

	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@Autowired
	private AcessoController acessoController;

	@Test
	void testCadastraAcesso() {
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE");
		acesso = acessoController.salvarAcesso(acesso).getBody();
		Assertions.assertEquals(true, acesso.getId() > 0);
	}

	@Test
	void contextLoads() {
	}

}
