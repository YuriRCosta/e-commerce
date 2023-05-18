package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.ExceptionECommerce;
import br.com.ecommerce.ecommerce.enums.TipoPessoa;
import br.com.ecommerce.ecommerce.model.PessoaFisica;
import br.com.ecommerce.ecommerce.model.PessoaJuridica;
import br.com.ecommerce.ecommerce.model.dto.CepDTO;
import br.com.ecommerce.ecommerce.model.dto.ConsultaCNPJDTO;
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

import java.util.List;
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
    public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionECommerce {
        if (pessoaJuridica.getCnpj() == null) {
            throw new ExceptionECommerce("CNPJ não pode ser nulo.");
        }
        if (pessoaJuridica.getTipoPessoal() == null) {
            throw new ExceptionECommerce("Tipo de pessoa não pode ser nulo.");
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
        if (pessoaFisica.getTipoPessoal() == null) {
            pessoaFisica.setTipoPessoal(TipoPessoa.FISICA.name());
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

    @GetMapping("/consultaNomePf/{nome}")
    public ResponseEntity<List<PessoaFisica>> findListByNamePf(@PathVariable String nome) {
        List<PessoaFisica> pessoaFisicaList = pessoaFisicaRepository.findByNomeList(nome);
        return ResponseEntity.ok(pessoaFisicaList);
    }

    @GetMapping("/consultaCpfPf/{cpf}")
    public ResponseEntity<PessoaFisica> findByCpf(@PathVariable String cpf) {
        PessoaFisica pessoaFisica = pessoaFisicaRepository.findByCpf(cpf);
        return ResponseEntity.ok(pessoaFisica);
    }

    @GetMapping("/listarPF")
    public ResponseEntity<List<PessoaFisica>> findListPf() {
        List<PessoaFisica> pessoaFisicaList = pessoaFisicaRepository.findAll();
        return ResponseEntity.ok(pessoaFisicaList);
    }

    @GetMapping("/consultaNomePj/{nome}")
    public ResponseEntity<List<PessoaJuridica>> findListByNamePj(@PathVariable String nome) {
        List<PessoaJuridica> pessoaJuridicaList = pessoaJuridicaRepository.findByNomeList(nome);
        return ResponseEntity.ok(pessoaJuridicaList);
    }

    @GetMapping("/consultaCnpjPj/{cnpj}")
    public ResponseEntity<PessoaJuridica> findByCnpj(@PathVariable String cnpj) {
        PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.findByCnpj(cnpj);
        return ResponseEntity.ok(pessoaJuridica);
    }

    @GetMapping("/listarPj")
    public ResponseEntity<List<PessoaJuridica>> findListPj() {
        List<PessoaJuridica> pessoaJuridicaList = pessoaJuridicaRepository.findAll();
        return ResponseEntity.ok(pessoaJuridicaList);
    }

    @GetMapping("/consultaCnpjReceita/{cnpj}")
    public ResponseEntity<ConsultaCNPJDTO> consultaCnpjReceitaWS(@PathVariable String cnpj) {
        ConsultaCNPJDTO cnpjDto = pessoaUserService.consultaCnpjWS(cnpj);
        return ResponseEntity.ok(cnpjDto);
    }

}
