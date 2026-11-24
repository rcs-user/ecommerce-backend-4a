package br.edu.unifio.ecommerce.recursos;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InicioRecurso {
    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return Map.of(
                "aplicacao", "ToAqui - Transporte Escolar",
                "status", "API em execução",
                "perfis", Map.of(
                        "admin", "Administrador",
                        "responsavel", "Responsável",
                        "aluno", "Aluno",
                        "monitor", "Monitor"));
    }
}
