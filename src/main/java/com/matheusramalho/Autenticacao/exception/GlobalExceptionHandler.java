package com.matheusramalho.Autenticacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Map<String, String>> senhaInvalida(SenhaInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro(ex.getMessage()));
    }

    @ExceptionHandler(UsuarioDuplicadoException.class)
    public ResponseEntity<Map<String, String>> usuarioDuplicado(UsuarioDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro(ex.getMessage()));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<Map<String, String>> acessoNegado(AcessoNegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro(ex.getMessage()));
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, String>> credenciaisInvalidas(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro(ex.getMessage()));
    }

    @ExceptionHandler(ContaBloqueadaException.class)
    public ResponseEntity<Map<String, String>> contaBloqueada(ContaBloqueadaException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro(ex.getMessage()));
    }

    @ExceptionHandler(MfaRequeridoException.class)
    public ResponseEntity<Map<String, Object>> mfaRequerido(MfaRequeridoException ex) {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("erro", ex.getMessage());
        corpo.put("mfaRequerido", true);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(corpo);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro(ex.getMessage()));
    }

    // erro de validacao dos DTOs (@NotBlank, @NotNull etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "Dados invalidos."
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro(mensagem));
    }

    private Map<String, String> erro(String mensagem) {
        Map<String, String> corpo = new HashMap<>();
        corpo.put("erro", mensagem);
        return corpo;
    }
}
