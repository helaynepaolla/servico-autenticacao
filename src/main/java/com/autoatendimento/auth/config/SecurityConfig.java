package com.autoatendimento.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
	private final CorsFilter corsFilter;

    // Injeta o corsFilter 
    public SecurityConfig(CorsFilter corsFilter) {
        this.corsFilter = corsFilter;
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	//Para que o Spring não bloqueie os endpoints em desenvolvimento, aqui o spring esta sendo instruido a ignorar o bloqueio padrão na URL /api/auth/**  e foi criado uma variavel private CorsFilter corsFilter.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
        // Ativa o filtro do CORS antes do filtro de segurança
        .addFilter(corsFilter) 
        // Desabilita o CSRF (necessário para APIs Stateless/Angular)
        .csrf(csrf -> csrf.disable()) 
        // Permite qualquer requisição mapeada em /api/auth/
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() 
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
