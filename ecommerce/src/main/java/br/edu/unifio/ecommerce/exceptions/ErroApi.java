package br.edu.unifio.ecommerce.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroApi(LocalDateTime timestamp, int status, String erro, String mensagem,
                      String caminho, Map<String, String> campos) {
}
