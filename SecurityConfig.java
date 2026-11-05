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
            .requestMatchers(HttpMethod.GET, "/", "/index.html", "/css/**",
                "/js/**", "/images/**", "/favicon.ico", "/error", "/csrf").permitAll()
            .requestMatchers(HttpMethod.GET, "/produto", "/produto/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/cliente", "/verificacao/verificado").permitAll()
            .requestMatchers(HttpMethod.GET, "/verificacao/usuario").permitAll()
            .requestMatchers(HttpMethod.GET, "/verificacao/admin-status").authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/cliente", "/cliente/**",
                "/pedido", "/pedido/**", "/reserva", "/reserva/**",
                "/item-pedido", "/item-pedido/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/produto", "/produto/**",
                "/ingrediente", "/ingrediente/**", "/mesa", "/mesa/**",
                "/cliente/pontos/**", "/pedido", "/pedido/**",
                "/item-pedido", "/item-pedido/**",
                "/item-pedido-ingrediente", "/item-pedido-ingrediente/**",
                "/reserva", "/reserva/**").hasRole("ADMIN")
            .requestMatchers("/ingrediente", "/ingrediente/**",
                "/mesa", "/mesa/**",
                "/item-pedido-ingrediente", "/item-pedido-ingrediente/**").hasRole("ADMIN")
            .anyRequest().denyAll()
        );
        http.formLogin(form -> form.usernameParameter("email").permitAll());
        http.logout(logout -> logout.permitAll());
        return http.build();
    }
}
