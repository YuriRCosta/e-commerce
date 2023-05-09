package br.com.ecommerce.ecommerce.security;

import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true)
public class WebConfigSecurity implements HttpSessionListener {

//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().requestMatchers(HttpMethod.GET, "/salvarAcesso")
//                .requestMatchers(HttpMethod.POST, "/salvarAcesso");
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests( (authorize) -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/salvarAcesso").permitAll()
                        .requestMatchers("/excluirAcesso").permitAll()
                        .requestMatchers("/excluirAcesso/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .requestMatchers("/static/**"); // #3
    }

}
