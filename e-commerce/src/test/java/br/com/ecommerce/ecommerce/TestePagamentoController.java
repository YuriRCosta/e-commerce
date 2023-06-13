package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.controller.PagamentoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest(classes = ECommerceApplication.class)
public class TestePagamentoController {

    @Autowired
    private PagamentoController pagamentoController;

    @Test
    public void testFinalizarCompraCartao() throws Exception {
        pagamentoController.finalizarCompraCartaoAsaas("5195599297788098", "Alex F Egidio", "315", "11", "2024", 42L, "04584572054", 2, "95555000", "Rua Teresopolis", "179", "RS", "Capao da Canoa");
    }

}
