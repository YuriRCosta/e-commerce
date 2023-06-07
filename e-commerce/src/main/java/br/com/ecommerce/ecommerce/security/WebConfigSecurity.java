package br.com.ecommerce.ecommerce.security;

import br.com.ecommerce.ecommerce.service.ImplementacaoUserDetailsService;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true)
public class WebConfigSecurity implements HttpSessionListener {

    @Autowired
    private AuthenticationConfiguration configuration;

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(implementacaoUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable().authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/index").permitAll()
                .requestMatchers("/requisicaojunoboleto/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS ,"/**").permitAll()
                .anyRequest().authenticated()
        ).logout().logoutSuccessUrl("/index").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                .addFilterAfter(new JWTLoginFilter("/login", configuration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTApiAAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers(HttpMethod.GET ,"/requisicaojunoboleto/**", "/notificacaoapiv2")
                .requestMatchers(HttpMethod.POST ,"/requisicaojunoboleto/**", "/notificacaoapiv2")
                .requestMatchers("/static/**"); // #3
    }

}
