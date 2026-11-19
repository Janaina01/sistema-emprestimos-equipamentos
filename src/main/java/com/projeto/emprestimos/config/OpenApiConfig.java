
package com.projeto.emprestimos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sistema de Empréstimos de Equipamentos",
                version = "1.0.0",
                description = "API REST para gerenciamento de equipamentos, colaboradores e empréstimos, com controle de disponibilidade, devoluções e atrasos."
        )
)
public class OpenApiConfig {
}
