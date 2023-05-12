package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @PostMapping("/salvarPJ")
    public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody PessoaJuridica pessoaJuridica) {
        Optional<PessoaJuridica> acessoExistente = Optional.ofNullable((pessoaRepository.findByCnpj(pessoaJuridica.getCnpj())));
        if (acessoExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ ja cadastrado.");
        }
        PessoaJuridica acessoSalvo = pessoaRepository.save(pessoaJuridica);
        return ResponseEntity.ok(acessoSalvo);
    }

}
