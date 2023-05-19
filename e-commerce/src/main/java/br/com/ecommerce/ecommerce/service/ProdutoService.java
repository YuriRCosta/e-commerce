package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.Produto;
import br.com.ecommerce.ecommerce.repository.ProdutoRepository;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    public void verificaIfs(Produto produto) throws ExceptionECommerce {
        if (produtoRepository.existsByNome(produto.getNome().toUpperCase())) {
            throw new ExceptionECommerce("Produto "+produto.getNome()+" ja cadastrado.");
        }
        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionECommerce("Empresa não informada.");
        }
        if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionECommerce("Categoria não informada.");
        }
        if (produto.getMarcaProduto().getId() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionECommerce("Marca não informada.");
        }
        if (produto.getDescricao().length() > 2000) {
            throw new ExceptionECommerce("Descrição não pode ser maior que 2000.");
        }
        if (produto.getQtdEstoque() < 1) {
            throw new ExceptionECommerce("Quantidade em estoque não pode ser menor que 1.");
        }
    }

    public void verificaImagem(Produto produto) throws ExceptionECommerce {
        if (produto.getImagemProdutos() == null || produto.getImagemProdutos().isEmpty()) {
            throw new ExceptionECommerce("Imagem não informada.");
        }
        if (produto.getImagemProdutos().size() < 3) {
            throw new ExceptionECommerce("Imagem deve conter no minimo 3 imagens.");
        }
        if (produto.getImagemProdutos().size() > 6) {
            throw new ExceptionECommerce("Imagem deve conter no maximo 6 imagens.");
        }
    }

    public void salvarImagem(Produto produto) throws IOException {
        if (produto.getId() == null) {
            for (int i = 0; i < produto.getImagemProdutos().size(); i++) {
                produto.getImagemProdutos().get(i).setProduto(produto);
                produto.getImagemProdutos().get(i).setEmpresa(produto.getEmpresa());

                String base64Image = "";

                if (produto.getImagemProdutos().get(i).getImagemOriginal().contains("data:image/jpeg;base64,")) {
                    base64Image = produto.getImagemProdutos().get(i).getImagemOriginal().split(",")[1];
                } else {
                    base64Image = produto.getImagemProdutos().get(i).getImagemOriginal();
                }

                byte[] imagemBytes = DatatypeConverter.parseBase64Binary(base64Image);

                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemBytes));

                salvaImagemMiniatura(bufferedImage, produto, i);
            }
        }
    }

    public void salvaImagemMiniatura(BufferedImage bufferedImage, Produto produto, int i) throws IOException {
        if (bufferedImage != null) {
            int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
            int largura = Integer.parseInt("800");
            int altura = Integer.parseInt("600");

            BufferedImage resizedImage = new BufferedImage(largura, altura, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, largura, altura, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "png", baos);

            String miniaturaBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());

            produto.getImagemProdutos().get(i).setImagemMiniatura(miniaturaBase64);

            bufferedImage.flush();
            resizedImage.flush();
            baos.flush();
            baos.close();
        }
    }

    public void alertaQtdEstoque(Produto produto) throws ExceptionECommerce, MessagingException, UnsupportedEncodingException {
        if (produto.getAlertaQtdEstoque() && produto.getQtdEstoque() <=1) {
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("<h2>Produto: "+produto.getNome()+"</h2>");
            mensagem.append("<h3>Quantidade em estoque: "+produto.getQtdEstoque()+"</h3>");
            mensagem.append("<h3> ID produto: "+produto.getId()+"</h3>");

            if (produto.getEmpresa().getEmail() != null) {
                serviceSendEmail.enviarEmailHtml( "Alerta de estoque", mensagem.toString(), produto.getEmpresa().getEmail());
            }
        }
    }
}
