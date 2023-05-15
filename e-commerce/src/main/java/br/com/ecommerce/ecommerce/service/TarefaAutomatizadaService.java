package br.com.ecommerce.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarefaAutomatizadaService {

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    //@Scheduled(initialDelay = 1000, fixedRate = 86400000)
    /*@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo")
    public void notificarUser() {
        StringBuilder conteudo = new StringBuilder();

        List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();

        for (Usuario usuario: usuarios) {
            conteudo.append("<p>Olá, " + usuario.getPessoa().getNome() + "</p>");
            conteudo.append("<p>Seu acesso ao sistema E Commerce está prestes a vencer, por favor, atualize sua senha!</p>");
            conteudo.append("<p>Atenciosamente, equipe E Commerce</p>");

            try {
                serviceSendEmail.enviarEmailHtml("Acesso ao sistema E Commerce", conteudo.toString(), usuario.getPessoa().getEmail());
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

}
