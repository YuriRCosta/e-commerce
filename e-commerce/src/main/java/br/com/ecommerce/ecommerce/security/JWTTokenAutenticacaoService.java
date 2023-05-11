package br.com.ecommerce.ecommerce.security;

import br.com.ecommerce.ecommerce.ApplicationContextLoad;
import br.com.ecommerce.ecommerce.model.Usuario;
import br.com.ecommerce.ecommerce.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTTokenAutenticacaoService {

    private static final long EXPIRATION_TIME = 604800000;

    private static final String SECRET = "SenhaExtremamenteSecreta";

    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    public void addAuthentication(HttpServletResponse response, String username) throws Exception {

            String JWT = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();

            String token = TOKEN_PREFIX + " " + JWT;

            response.addHeader(HEADER_STRING, token);
            liberacaoCors(response);
            response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

    public Authentication getAuthentication(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String token = request.getHeader(HEADER_STRING);
        try {
            if(token != null) {
                String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

                String user = Jwts.parser().setSigningKey(SECRET)
                        .parseClaimsJws(tokenLimpo)
                        .getBody()
                        .getSubject();

                if(user != null) {
                    Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class).findByLogin(user);

                    if(usuario != null) {
                        return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            try {
                response.getOutputStream().println("Seu token está expirado, faça o login ou informe um novo token para autenticação");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        liberacaoCors(response);
        return null;
    }

    private void liberacaoCors(HttpServletResponse response) {
        if(response.getHeader("Access-Control-Allow-Origin") == null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }

        if(response.getHeader("Access-Control-Allow-Headers") == null) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }

        if(response.getHeader("Access-Control-Request-Headers") == null) {
            response.addHeader("Access-Control-Request-Headers", "*");
        }

        if(response.getHeader("Access-Control-Allow-Methods") == null) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
