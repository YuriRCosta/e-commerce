package br.com.ecommerce.ecommerce.service;

import br.com.ecommerce.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

}
