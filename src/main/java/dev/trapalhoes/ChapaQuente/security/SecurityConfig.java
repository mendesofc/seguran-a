package dev.trapalhoes.ChapaQuente.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/", "/index.html", "/css/**", "/js/**",
                "/images/**", "/favicon.ico", "/error", "/csrf").permitAll()
            .requestMatchers(HttpMethod.GET, "/produto", "/produto/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/cliente", "/verificacao/verificado").permitAll()
            .requestMatchers(HttpMethod.GET, "/verificacao/usuario").permitAll()
            .requestMatchers("/verificacao/admin", "/verificacao/admin-status", "/admin/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/cliente", "/cliente/**", "/pedido", "/pedido/**",
                "/reserva", "/reserva/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/cliente/pontos/**", "/pedido/**", "/produto/**",
                "/ingrediente/**", "/mesa/**", "/item-pedido/**",
                "/item-pedido-ingrediente/**", "/reserva/status/**").hasRole("ADMIN")
            .requestMatchers("/ingrediente/**", "/mesa/**", "/item-pedido-ingrediente/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/item-pedido/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/reserva").hasAnyRole("ADMIN", "CLIENTE")
            .anyRequest().denyAll()
        );
        http.formLogin(form -> form.usernameParameter("email").permitAll());
        http.logout(logout -> logout.permitAll());
        return http.build();
    }
}