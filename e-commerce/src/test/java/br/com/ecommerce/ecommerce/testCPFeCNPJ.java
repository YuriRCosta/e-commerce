package br.com.ecommerce.ecommerce;

import br.com.ecommerce.ecommerce.util.ValidaCNPJ;
import br.com.ecommerce.ecommerce.util.ValidaCPF;

public class testCPFeCNPJ {

    public static void main(String[] args) {
        boolean isCnpj = ValidaCNPJ.isCNPJ("11.348.130/0001-71");

        System.out.println("Cenpj eh: " + isCnpj);

        boolean isCpf = ValidaCPF.isCPF("12312423151");

        System.out.println("CPF eh: " + isCpf);
    }



}
