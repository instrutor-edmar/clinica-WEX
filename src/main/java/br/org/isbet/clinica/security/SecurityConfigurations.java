package br.org.isbet.clinica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations {
    private SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // O Spring já chama o método aqui
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/pacientes/register").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/medicos/register").permitAll();
                    
                    req.requestMatchers(
                        "/",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/api-docs/**"
                    ).permitAll();

                    req.requestMatchers(HttpMethod.POST, "/auth/register").hasAuthority("ROLE_ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/auth/validarMedico/{id}").hasAuthority("ROLE_ADMIN");
                    req.requestMatchers("/auth/validarMedico").hasAuthority("ROLE_ADMIN");

                    req.requestMatchers(HttpMethod.GET, "/pacientes/buscarPorNome").hasAuthority("ROLE_ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/pacientes/listar").hasAuthority("ROLE_ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/pacientes/{id}").hasAuthority("ROLE_ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/pacientes").hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE");
                    req.requestMatchers(HttpMethod.GET, "/pacientes/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE");

                    req.requestMatchers(HttpMethod.GET, "/medicos/listar").hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE", "ROLE_MEDICO");
                    req.requestMatchers(HttpMethod.GET, "/medicos/buscarPorNome").hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE");
                    req.requestMatchers(HttpMethod.DELETE, "/medicos/{id}").hasAuthority("ROLE_ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/medicos").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO");
                    req.requestMatchers(HttpMethod.GET, "/medicos/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO");

                    req.requestMatchers("/consultas/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE", "ROLE_MEDICO");

                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()))
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token JWT não informado ou inválido.\"}");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        
        // Importante: O Swagger precisa enviar os cabeçalhos de Authorization (Bearer Token)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        
        // Permitir credenciais (Cookies, Basic Auth, etc) junto com padrões coringa (*)
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
}