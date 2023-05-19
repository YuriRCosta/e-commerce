package br.com.ecommerce.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "imagem_produto")
@SequenceGenerator(name = "seq_imagem_produto", sequenceName = "seq_imagem_produto", allocationSize = 1, initialValue = 1)
public class ImagemProduto implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_imagem_produto")
    private Long id;

    @NotNull(message = "A empresa não pode ser nula.")
    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private PessoaJuridica empresa;

    @NotNull(message = "A imagem original não pode ser nula.")
    @Column(columnDefinition = "text", nullable = false)
    private String imagemOriginal;

    @NotNull(message = "A imagem miniatura não pode ser nula.")
    @Column(columnDefinition = "text", nullable = false)
    private String imagemMiniatura;

    @NotNull(message = "O produto não pode ser nulo.")
    @JsonIgnoreProperties(allowGetters = true)
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
    private Produto produto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImagemProduto that = (ImagemProduto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public PessoaJuridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PessoaJuridica empresa) {
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagemOriginal() {
        return imagemOriginal;
    }

    public void setImagemOriginal(String imagemOriginal) {
        this.imagemOriginal = imagemOriginal;
    }

    public String getImagemMiniatura() {
        return imagemMiniatura;
    }

    public void setImagemMiniatura(String imagemMiniatura) {
        this.imagemMiniatura = imagemMiniatura;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
