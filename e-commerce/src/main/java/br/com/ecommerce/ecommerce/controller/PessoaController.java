package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.model.PessoaFisica;
import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.model.dto.CepDTO;
import br.com.ecommerce.ecommerce.repository.EnderecoRepository;
import br.com.ecommerce.ecommerce.repository.PessoaFisicaRepository;
import br.com.ecommerce.ecommerce.repository.PessoaJuridicaRepository;
import br.com.ecommerce.ecommerce.service.PessoaUserService;
import br.com.ecommerce.ecommerce.util.ValidaCNPJ;
import br.com.ecommerce.ecommerce.util.ValidaCPF;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PessoaController {

    @Autowired
    private PessoaUserService pessoaUserService;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @GetMapping("/consultaCep/{cep}")
    public ResponseEntity<CepDTO> consultaCep(@PathVariable String cep) {
        CepDTO cepDTO = pessoaUserService.consultaCep(cep);
        return ResponseEntity.ok(cepDTO);
    }

    @PostMapping("/salvarPJ")
    public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionECommerce {
        if (pessoaJuridica.getCnpj() == null) {
            throw new ExceptionECommerce("CNPJ não pode ser nulo.");
        }
        Optional<PessoaJuridica> acessoExistente = Optional.ofNullable((pessoaJuridicaRepository.findByCnpj(pessoaJuridica.getCnpj())));
        if (acessoExistente.isPresent()) {
            throw new ExceptionECommerce("CNPJ "+pessoaJuridica.getCnpj()+" ja cadastrado.");
        }
        if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
            throw new ExceptionECommerce("CNPJ "+pessoaJuridica.getCnpj()+" invalido.");
        }
        pessoaUserService.enderecoApiPJ(pessoaJuridica);

        PessoaJuridica acessoSalvo = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
        return ResponseEntity.ok(acessoSalvo);
    }

    @PostMapping("/salvarPF")
    public ResponseEntity<PessoaFisica> salvarPF(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionECommerce {
        if (pessoaFisica.getCpf() == null) {
            throw new ExceptionECommerce("CPF não pode ser nulo.");
        }
        Optional<PessoaFisica> acessoExistente = Optional.ofNullable((pessoaFisicaRepository.findByCpf(pessoaFisica.getCpf())));
        if (acessoExistente.isPresent()) {
            throw new ExceptionECommerce("CPF "+pessoaFisica.getCpf()+" ja cadastrado.");
        }
        if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
            throw new ExceptionECommerce("CPF "+pessoaFisica.getCpf()+" invalido.");
        }
        pessoaUserService.enderecoApiPF(pessoaFisica);
        PessoaFisica acessoSalvo = pessoaUserService.salvarPessoaFisica(pessoaFisica);
        return ResponseEntity.ok(acessoSalvo);
    }

}
