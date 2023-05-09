package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.model.Acesso;
import br.com.ecommerce.ecommerce.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {

    @Autowired
    private AcessoRepository acessoRepository;

    public Acesso save(Acesso acesso) {
        return acessoRepository.save(acesso);
    }
}
