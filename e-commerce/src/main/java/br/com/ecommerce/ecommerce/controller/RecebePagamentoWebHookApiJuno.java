package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.BoletoJuno;
import br.com.ecommerce.ecommerce.model.dto.DataNotificacaoApiJunoPagamento;
import br.com.ecommerce.ecommerce.repository.BoletoJunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/requisicaojunoboleto")
public class RecebePagamentoWebHookApiJuno {

    @Autowired
    private BoletoJunoRepository boletoJunoRepository;

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
