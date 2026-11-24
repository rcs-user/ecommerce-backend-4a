package br.edu.unifio.ecommerce.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class TratadorGlobalExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroApi> naoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ConflitoDeNegocioException.class)
    public ResponseEntity<ErroApi> conflito(ConflitoDeNegocioException ex, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApi> validacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos na requisição", request, campos);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroApi> parametrosInvalidos(ConstraintViolationException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroApi> corpoInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou JSON malformado", request, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroApi> parametroInvalido(MethodArgumentTypeMismatchException ex,
                                                     HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST,
                "Parâmetro '" + ex.getName() + "' possui valor ou tipo inválido", request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroApi> integridade(DataIntegrityViolationException ex,
                                               HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT,
                "Operação viola uma regra de integridade ou relação existente", request, null);
    }

    private ResponseEntity<ErroApi> resposta(HttpStatus status, String mensagem, HttpServletRequest request,
                                             Map<String, String> campos) {
        ErroApi erro = new ErroApi(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                mensagem, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(erro);
    }
}
