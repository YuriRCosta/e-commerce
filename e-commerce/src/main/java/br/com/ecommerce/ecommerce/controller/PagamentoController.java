package br.com.ecommerce.ecommerce.controller;

import br.com.ecommerce.ecommerce.model.VendaCompraLojaVirtual;
import br.com.ecommerce.ecommerce.model.dto.VendaCompraLojaVirtualDTO;
import br.com.ecommerce.ecommerce.repository.VendaCompraLojaVirtualRepository;
import br.com.ecommerce.ecommerce.service.VendaCompraLojaVirtualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagamentoController {

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private VendaCompraLojaVirtualService vendaCompraLojaVirtualSer;

    @RequestMapping(method = RequestMethod.GET, value = "/pagamento/{id}")
    public ModelAndView pagamentoBoleto(@PathVariable(value = "id", required = false) String id){
        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(Long.parseLong(id));

        ModelAndView modelAndView = new ModelAndView("pagamento");

        if (vendaCompraLojaVirtual == null) {
            modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
        }else {
            modelAndView.addObject("venda", vendaCompraLojaVirtualSer.consultaVenda(vendaCompraLojaVirtual));
        }

        return modelAndView;
    }

}
