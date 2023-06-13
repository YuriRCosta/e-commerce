package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.BoletoJuno;
import br.com.ecommerce.ecommerce.model.dto.DataNotificacaoApiJunoPagamento;
import br.com.ecommerce.ecommerce.model.dto.NotificacaoPagamentoAsaas;
import br.com.ecommerce.ecommerce.repository.BoletoJunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/requisicaojunoboleto")
public class RecebePagamentoWebHookApiJuno {

    @Autowired
    private BoletoJunoRepository boletoJunoRepository;

    @ResponseBody
    @RequestMapping(value = "/notificacaoapiasaas", consumes = {"application/json;charset=UTF-8"},
            headers = "Content-Type=application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResponseEntity<String> recebeNotificacaoPagamentoApiAsaas(@RequestBody NotificacaoPagamentoAsaas notificacaoPagamentoApiAsaas) {
        BoletoJuno boletoJuno = boletoJunoRepository.findByCodigoBoletoPix(notificacaoPagamentoApiAsaas.idFatura());

        if (boletoJuno == null) {
            return new ResponseEntity<>("Boleto/Fatura não encontrada no banco de dados", HttpStatus.OK);
        }


        if (boletoJuno != null
                && notificacaoPagamentoApiAsaas.boletoPixFaturaPaga()
                && !boletoJuno.isQuitado()) {

            boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
            System.out.println("Boleto: " + boletoJuno.getCode() + " foi quitado ");
            /**Fazendo qualquer regra de negocio que vc queira*/

            return ResponseEntity.ok("Recebido do Asaas, boleto id: " + boletoJuno.getId());
        }else {
            System.out.println("Fatura :"
                    + notificacaoPagamentoApiAsaas.idFatura()
                    + " não foi processada, quitada: "
                    + notificacaoPagamentoApiAsaas.boletoPixFaturaPaga()
                    + " valor quitado : "+ boletoJuno.isQuitado());
        }

        return ResponseEntity.ok("Não foi processado a fatura : " + notificacaoPagamentoApiAsaas.idFatura());
    }

    public RecebePagamentoWebHookApiJuno(BoletoJunoRepository boletoJunoRepository) {
        this.boletoJunoRepository = boletoJunoRepository;
    }

    @PostMapping(value = "/notificacaoapiv2", headers = "Content-Type=application/json;charset=UTF-8", consumes = "application/json", produces = "application/json")
    private HttpStatus recebeNotificacaoPagamento(@RequestBody DataNotificacaoApiJunoPagamento dataNotificacaoApiJunoPagamento) {
        dataNotificacaoApiJunoPagamento.getData().forEach((data) -> {
            String codigoBoletoPix = data.getAttributes().getCharge().getCode();

            String status = data.getAttributes().getStatus();

            boolean boletoPago = status.equals("CONFIRMED");

            BoletoJuno boletoJuno = boletoJunoRepository.findByCodigoBoletoPix(codigoBoletoPix);

            if (boletoJuno != null && !boletoJuno.isQuitado() && boletoPago) {
                boletoJunoRepository.quitarBoletoJuno(boletoJuno.getId());
            }
        });
        return HttpStatus.OK;
    }

}
