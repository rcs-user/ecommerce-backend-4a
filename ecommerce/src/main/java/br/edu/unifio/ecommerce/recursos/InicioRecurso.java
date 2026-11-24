package br.edu.unifio.ecommerce.recursos;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InicioRecurso {

    @GetMapping("/api/status")
    public Map<String, Object> inicio() {
        return Map.of(
                "aplicacao", "Projeto Integrador - E-commerce",
                "status", "API em execução",
                "documentacao", "README.md e docs/documentacao-api.md",
                "usuarios", Map.of(
                        "admin", "ADMIN",
                        "vendedor", "VENDEDOR"),
                "endpointAutenticado", "/produtos");
    }
}
