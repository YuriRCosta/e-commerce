package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.dto.ObjetoReqRelatorioProdutoAlertaEstoqueDTO;
import br.com.ecommerce.ecommerce.model.dto.ObjetoReqRelatorioProdutoCompraDTO;
import br.com.ecommerce.ecommerce.model.dto.ObjetoReqRelatorioStatusCompraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaFiscalCompraService {

    private static final String SQL = new StringBuilder().append("SELECT p.id AS codigoProduto, p.nome AS nomeProduto, p.valor_venda AS valorVendaProduto, ")
            .append("nip.quantidade AS quantidadeComprada, pj.nome AS nomeFornecedor, pj.id  AS codigoFornecedor, nfc.data_compra AS dataCompra, " +
                    "p.qtd_estoque AS qtdEstoque,  p.qtd_alerta_estoque AS qtdAlertaEstoque, p.alerta_qtd_estoque AS alertaQtdEstoque ")
            .append("FROM nota_fiscal_compra AS nfc ").append("INNER JOIN nota_item_produto AS nip ON nfc.id = nota_fiscal_compra_id ")
            .append("INNER JOIN produto AS p ON p.id = nip.produto_id ").append("INNER JOIN pessoa_juridica AS pj ON pj.id = nfc.pessoa_id where ").toString();

    private static final String SQL_STATUS = new StringBuilder().append(
            "SELECT p.id AS codigoProduto, " +
            " p.nome AS nomeProduto,  " +
            " pf.email AS emailCliente, " +
            " pf.telefone AS foneCliente, " +
            " p.valor_venda AS valorVendaProduto,  " +
            "ivl.quantidade AS quantidadeComprada, " +
            " pf.nome AS nomeCliente,  " +
            " vlv.status_venda_loja_virtual AS statusVenda, " +
            " pf.id  AS codigoCliente,  " +
            " p.qtd_estoque AS quantidadeEstoque " +
            "FROM venda_compra_loja_virtual AS vlv " +
            "INNER JOIN item_venda_loja AS ivl ON ivl.venda_compra_loja_virtual_id = vlv.id  " +
            "INNER JOIN produto AS p ON p.id = ivl.produto_id  " +
            "INNER JOIN pessoa_fisica AS pf ON pf.id = vlv.pessoa_id " +
            "WHERE UPPER(p.nome) LIKE UPPER('%iphone%')").toString();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

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

    public List<ObjetoReqRelatorioProdutoAlertaEstoqueDTO> gerarRelatorioAlertaEstoque(ObjetoReqRelatorioProdutoAlertaEstoqueDTO objetoReqRelatorioProdutoAlertaEstoqueDTO) {
        List<ObjetoReqRelatorioProdutoAlertaEstoqueDTO> retorno = new ArrayList<>();
        String sql = SQL + " p.alerta_qtd_estoque = true and p.qtd_estoque <= p.qtd_alerta_estoque";

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(objetoReqRelatorioProdutoAlertaEstoqueDTO.getClass()));
        return retorno;
    }

    public List<ObjetoReqRelatorioStatusCompraDTO> gerarRelatorioStatusCompra(ObjetoReqRelatorioStatusCompraDTO objetoReqRelatorioStatusCompraDTO) {
        List<ObjetoReqRelatorioStatusCompraDTO> retorno = new ArrayList<>();
        String sql = SQL_STATUS + " and vlv.status_venda_loja_virtual = '" + objetoReqRelatorioStatusCompraDTO.getStatusVenda() + "'";

        if (objetoReqRelatorioStatusCompraDTO.getNomeProduto() != null && !objetoReqRelatorioStatusCompraDTO.getNomeProduto().isEmpty()) {
            sql += " AND p.nome LIKE '%" + objetoReqRelatorioStatusCompraDTO.getNomeProduto() + "%'";
        }
        if (objetoReqRelatorioStatusCompraDTO.getNomeCliente() != null && !objetoReqRelatorioStatusCompraDTO.getNomeCliente().isEmpty()) {
            sql += " AND pf.nome LIKE '%" + objetoReqRelatorioStatusCompraDTO.getNomeCliente() + "%'";
        }

        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(objetoReqRelatorioStatusCompraDTO.getClass()));
        return retorno;
    }
}
