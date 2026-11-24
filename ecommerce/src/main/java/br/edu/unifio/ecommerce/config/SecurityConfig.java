package br.edu.unifio.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpStatus;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(Customizer.withDefaults());
        http.exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.setHeader("WWW-Authenticate", "Basic realm=\"E-commerce API\"");
                    response.getWriter().write(
                            "{\"status\":401,\"erro\":\"Unauthorized\",\"mensagem\":\"Autenticação obrigatória ou credenciais inválidas\"}");
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    response.getWriter().write(
                            "{\"status\":403,\"erro\":\"Forbidden\",\"mensagem\":\"Usuário autenticado sem permissão para esta operação\"}");
                }));
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/error").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(HttpMethod.PUT, "/**").hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(HttpMethod.PATCH, "/**").hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "VENDEDOR")
                .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService users(PasswordEncoder encoder) {
        var admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        var vendedor = User.withUsername("vendedor")
                .password(encoder.encode("vendedor123"))
                .roles("VENDEDOR")
                .build();
        return new InMemoryUserDetailsManager(admin, vendedor);
    }
}
