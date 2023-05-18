package br.com.ecommerce.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "produto")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
    private Long id;

    @NotNull(message = "O tipo de unidade não pode ser nulo.")
    @NotBlank(message = "O tipo de unidade é obrigatório.")
    @Column(nullable = false)
    private String tipoUnidade;

    @NotNull(message = "O nome do produto não pode ser nulo.")
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "A descrição do produto não pode ser nula.")
    @NotBlank(message = "A descrição do produto é obrigatória.")
    @Column(nullable = false, columnDefinition = "text", length = 2000)
    private String descricao;

    @NotNull(message = "O peso do produto não pode ser nulo.")
    @Column(nullable = false)
    private Double peso;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private PessoaJuridica empresa;

    @NotNull(message = "A largura do produto não pode ser nula.")
    @Column(nullable = false)
    private Double largura;

    @NotNull(message = "A altura do produto não pode ser nula.")
    @Column(nullable = false)
    private Double altura;

    @NotNull(message = "A profundidade do produto não pode ser nula.")
    @Column(nullable = false)
    private Double profundidade;

    @NotNull(message = "O valor de custo do produto não pode ser nulo.")
    @Column(nullable = false)
    private BigDecimal valorVenda = BigDecimal.ZERO;

    @NotNull(message = "A quantidade de estoque do produto não pode ser nulo.")
    @Column(nullable = false)
    private Integer qtdEstoque = 0;

    @NotNull(message = "A categoria do produto não pode ser nulo.")
    @ManyToOne(targetEntity = CategoriaProduto.class)
    @JoinColumn(name = "categoria_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_produto_fk"))
    private CategoriaProduto categoriaProduto;

    @NotNull(message = "A marca do produto não pode ser nulo.")
    @ManyToOne(targetEntity = MarcaProduto.class)
    @JoinColumn(name = "marca_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "marca_produto_fk"))
    private MarcaProduto marcaProduto = new MarcaProduto();

    @NotNull(message = "A nota do produto não pode ser nulo.")
    @ManyToOne(targetEntity = NotaItemProduto.class)
    @JoinColumn(name = "nota_item_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nota_item_produto_fk"))
    private NotaItemProduto notaItemProduto = new NotaItemProduto();

    private Integer qtdAlertaEstoque = 0;

    private String linkYoutube;

    private Boolean alertaQtdEstoque = Boolean.FALSE;

    private Integer qtdClique = 0;

    @NotNull(message = "O produto deve estar ativo ou inativo.")
    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public NotaItemProduto getNotaItemProduto() {
        return notaItemProduto;
    }

    public void setNotaItemProduto(NotaItemProduto notaItemProduto) {
        this.notaItemProduto = notaItemProduto;
    }

    public MarcaProduto getMarcaProduto() {
        return marcaProduto;
    }

    public void setMarcaProduto(MarcaProduto marcaProduto) {
        this.marcaProduto = marcaProduto;
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public PessoaJuridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PessoaJuridica empresa) {
        this.empresa = empresa;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoUnidade() {
        return tipoUnidade;
    }

    public void setTipoUnidade(String tipoUnidade) {
        this.tipoUnidade = tipoUnidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getLargura() {
        return largura;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(Double profundidade) {
        this.profundidade = profundidade;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public Integer getQtdAlertaEstoque() {
        return qtdAlertaEstoque;
    }

    public void setQtdAlertaEstoque(Integer qtdAlertaEstoque) {
        this.qtdAlertaEstoque = qtdAlertaEstoque;
    }

    public String getLinkYoutube() {
        return linkYoutube;
    }

    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
    }

    public Boolean getAlertaQtdEstoque() {
        return alertaQtdEstoque;
    }

    public void setAlertaQtdEstoque(Boolean alertaQtdEstoque) {
        this.alertaQtdEstoque = alertaQtdEstoque;
    }

    public Integer getQtdClique() {
        return qtdClique;
    }

    public void setQtdClique(Integer qtdClique) {
        this.qtdClique = qtdClique;
    }
}
