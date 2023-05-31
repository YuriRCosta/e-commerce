package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.dto.ObjetoReqRelatorioProdutoCompraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaFiscalCompraService {

    private static final String SQL = "SELECT p.id AS codigoProduto, p.nome AS nomeProduto, p.valor_venda AS valorVendaProduto, " +
            "nip.quantidade AS quantidadeComprada, pj.nome AS nomeFornecedor, pj.id  AS codigoFornecedor, nfc.data_compra AS dataCompra " +
            "FROM nota_fiscal_compra AS nfc " +
            "INNER JOIN nota_item_produto AS nip ON nfc.id = nota_fiscal_compra_id " +
            "INNER JOIN produto AS p ON p.id = nip.produto_id " +
            "INNER JOIN pessoa_juridica AS pj ON pj.id = nfc.pessoa_id where ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ObjetoReqRelatorioProdutoCompraDTO> gerarRelatorioProdutoCompra(ObjetoReqRelatorioProdutoCompraDTO objetoReqRelatorioProdutoCompraDTO) {
        List<ObjetoReqRelatorioProdutoCompraDTO> retorno = new ArrayList<>();
        String sql = SQL + "nfc.data_compra BETWEEN '" + objetoReqRelatorioProdutoCompraDTO.getDataInicial() + "' AND '" + objetoReqRelatorioProdutoCompraDTO.getDataFinal() + "'";
        if(objetoReqRelatorioProdutoCompraDTO.getCodigoProduto() != null && !objetoReqRelatorioProdutoCompraDTO.getCodigoProduto().isEmpty()) {
            sql += " AND p.id = " + objetoReqRelatorioProdutoCompraDTO.getCodigoProduto();
        }
        if(objetoReqRelatorioProdutoCompraDTO.getCodigoNota() != null && !objetoReqRelatorioProdutoCompraDTO.getCodigoNota().isEmpty()) {
            sql += " AND nfc.id = " + objetoReqRelatorioProdutoCompraDTO.getCodigoNota();
        }
        if(objetoReqRelatorioProdutoCompraDTO.getCodigoFornecedor() != null && !objetoReqRelatorioProdutoCompraDTO.getCodigoFornecedor().isEmpty()) {
            sql += " AND pj.id = " + objetoReqRelatorioProdutoCompraDTO.getCodigoFornecedor();
        }
        if (objetoReqRelatorioProdutoCompraDTO.getNomeProduto() != null && !objetoReqRelatorioProdutoCompraDTO.getNomeProduto().isEmpty()) {
            sql += " AND p.nome LIKE '%" + objetoReqRelatorioProdutoCompraDTO.getNomeProduto() + "%'";
        }
        if (objetoReqRelatorioProdutoCompraDTO.getNomeFornecedor() != null && !objetoReqRelatorioProdutoCompraDTO.getNomeFornecedor().isEmpty()) {
            sql += " AND pj.nome LIKE '%" + objetoReqRelatorioProdutoCompraDTO.getNomeFornecedor() + "%'";
        }
        if (objetoReqRelatorioProdutoCompraDTO.getValorVendaProduto() != null && !objetoReqRelatorioProdutoCompraDTO.getValorVendaProduto().isEmpty()) {
            sql += " AND p.valor_venda = " + objetoReqRelatorioProdutoCompraDTO.getValorVendaProduto();
        }
        if (objetoReqRelatorioProdutoCompraDTO.getQuantidadeComprada() != null && !objetoReqRelatorioProdutoCompraDTO.getQuantidadeComprada().isEmpty()) {
            sql += " AND nip.quantidade = " + objetoReqRelatorioProdutoCompraDTO.getQuantidadeComprada();
        }
        if (objetoReqRelatorioProdutoCompraDTO.getDataCompra() != null && !objetoReqRelatorioProdutoCompraDTO.getDataCompra().isEmpty()) {
            sql += " AND nfc.data_compra = '" + objetoReqRelatorioProdutoCompraDTO.getDataCompra() + "'";
        }
        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(objetoReqRelatorioProdutoCompraDTO.getClass()));
        return retorno;
    }

}
