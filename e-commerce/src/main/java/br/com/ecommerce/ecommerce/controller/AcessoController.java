package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AcessoController {

    @Autowired
    private AcessoService acessoService;

    @PostMapping("/salvarAcesso")
    public Acesso salvarAcesso(Acesso acesso) {
        return acessoService.save(acesso);
    }
}
