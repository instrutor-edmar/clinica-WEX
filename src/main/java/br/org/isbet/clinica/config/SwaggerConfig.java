package br.org.isbet.clinica.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        // Criamos o servidor usando apenas "/" para que o Swagger use a URL atual do Codespaces
        Server localServer = new Server().url("/").description("Servidor Atual (Codespaces / Local)");
        
        return new OpenAPI()
                .info(new Info()
                        .title("API de Clínica médica")
                        .version("1.0")
                        .description("Documentação da API de Clínica médica"))
                .servers(List.of(localServer)) // Injeta o servidor relativo aqui
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira apenas o token JWT obtido no login.")));
    }
//     @Bean
//     public OpenApiCustomizer customerServerUrlOpenApiCustomizer() {
//         return openApi -> {
//             // Captura a URL base que o seu navegador usou para acessar a página
//             String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            
//             // Cria o servidor dinâmico apontando para onde o navegador está olhando
//             Server servidorAutomatico = new Server();
//             servidorAutomatico.setUrl(baseUrl);
//             servidorAutomatico.setDescription("Servidor Detectado Automaticamente");
            
//             // Injeta a URL descoberta dentro da lista de servidores do Swagger
//             openApi.setServers(List.of(servidorAutomatico));
//         };
//     }
}