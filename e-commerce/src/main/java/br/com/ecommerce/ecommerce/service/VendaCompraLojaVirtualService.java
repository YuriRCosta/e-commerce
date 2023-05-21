package br.com.ecommerce.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaCompraLojaVirtualService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void exclusaoTotalBanco(Long id) {
        String value = "begin;"
                + "update nota_fiscal_venda set venda_compra_loja_virtual_id = null where venda_compra_loja_virtual_id = "+id+";"
                + "delete from nota_fiscal_venda where venda_compra_loja_virtual_id = "+id+";"
                + "delete from item_venda_loja where venda_compra_loja_virtual_id = "+id+";"
                + "delete from status_rastreio where venda_compra_loja_virtual_id = "+id+";"
                + "delete from venda_compra_loja_virtual where id = "+id+";"
                + "commit;";

        jdbcTemplate.execute(value);
    }
}
