package br.edu.unifio.ecommerce.exceptions;

public class ConflitoDeNegocioException extends RuntimeException {
    public ConflitoDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
