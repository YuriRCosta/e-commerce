package br.com.ecommerce.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTApiAAutenticacaoFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = null;
        try {
            //authentication = new JWTTokenAutenticacaoService().getAuthentication((HttpServletResponse) response, (HttpServletRequest) request);

            //SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Erro ao autenticar usuário!");
        }
    }
}
