package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.enums.ApiTokenIntegracao;
import br.com.ecommerce.ecommerce.model.ItemVendaLoja;
import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.model.dto.EmpresaTransporteDTO;
import br.com.ecommerce.ecommerce.model.dto.ItemVendaDTO;
import br.com.ecommerce.ecommerce.model.dto.VendaCompraLojaVirtualDTO;
import br.com.ecommerce.ecommerce.model.dto.frete.DeliveryRange;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VendaCompraLojaVirtualService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    public void exclusaoTotalBanco2(Long id) {
        String sql = "begin; update venda_compra_loja_virtual set excluido = true where id = "+id+"; commit;";

        jdbcTemplate.execute(sql);
    }

    public void ativaVenda(Long id) {
        String sql = "begin; update venda_compra_loja_virtual set excluido = false where id = "+id+"; commit;";

        jdbcTemplate.execute(sql);
    }

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

    public List<EmpresaTransporteDTO> consultarFrete(String json) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/calculate")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO)
                .addHeader("User-Agent", "n0xfps1@gmail.com")
                .build();

        Response response = client.newCall(request).execute();
        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
        Iterator<JsonNode> jsonNodeIterator = jsonNode.iterator();
        List<EmpresaTransporteDTO> empresaTransporteDTOList = new ArrayList<>();

        while (jsonNodeIterator.hasNext()) {
            JsonNode node = jsonNodeIterator.next();
            EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();

            if (node.get("error") == null) {
                empresaTransporteDTO.setId(node.get("id").asText());
                empresaTransporteDTO.setNome(node.get("name").asText());
                empresaTransporteDTO.setValor(node.get("price").asText());
                empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
                empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
                empresaTransporteDTO.setDelivery_range(new ObjectMapper().readValue(node.get("delivery_range").toString(), DeliveryRange.class));

                empresaTransporteDTOList.add(empresaTransporteDTO);
            }
        }

        return empresaTransporteDTOList;
    }

    public VendaCompraLojaVirtualDTO consultaVenda(VendaCompraLojaVirtual compraLojaVirtual) {


        VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

        compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
        compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());

        compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
        compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());

        compraLojaVirtualDTO.setValorDesc(compraLojaVirtual.getValorDesconto());
        compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
        compraLojaVirtualDTO.setId(compraLojaVirtual.getId());

        for (ItemVendaLoja item : compraLojaVirtual.getItensVendaLoja()) {

            ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
            itemVendaDTO.setQuantidade(item.getQuantidade());
            itemVendaDTO.setProduto(item.getProduto());

            compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
        }

        return compraLojaVirtualDTO;
    }
}
