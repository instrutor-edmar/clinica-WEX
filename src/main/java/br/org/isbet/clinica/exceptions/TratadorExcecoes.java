// package br.org.isbet.clinica.exceptions;

// import jakarta.persistence.EntityNotFoundException;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import javax.naming.AuthenticationException;
// import java.nio.file.AccessDeniedException;

// @RestControllerAdvice
// public class TratadorExcecoes {
//     @ExceptionHandler(EntityNotFoundException.class)
//     public ResponseEntity tratarErro404() {
//         return ResponseEntity.notFound().build();
//     }

//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
//         var erros = ex.getFieldErrors();
//         return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
//     }

//     @ExceptionHandler(HttpMessageNotReadableException.class)
//     public ResponseEntity tratarErro400(HttpMessageNotReadableException ex) {
//         return ResponseEntity.badRequest().body("Erro de formatação JSON ou valor inválido para um dos campos (ex: Enum). Verifique os dados enviados.");
//     }

//     @ExceptionHandler(IllegalArgumentException.class)
//     public ResponseEntity tratarErroRegraDeNegocio(IllegalArgumentException ex) {
//         return ResponseEntity.badRequest().body(ex.getMessage());
//     }

//     @ExceptionHandler(BadCredentialsException.class)
//     public ResponseEntity tratarErroBadCredentials() {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
//     }

//     @ExceptionHandler(AuthenticationException.class)
//     public ResponseEntity tratarErroAuthentication() {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
//     }

//     @ExceptionHandler(AccessDeniedException.class)
//     public ResponseEntity tratarErroAcessoNegado() {
//         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
//     }

//     @ExceptionHandler(Exception.class)
//     public ResponseEntity tratarErro500(Exception ex) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
//     }

//     private record DadosErroValidacao(String campo, String mensagem) {
//         public DadosErroValidacao(FieldError erro) {
//             this(erro.getField(), erro.getDefaultMessage());
//         }
//     }
// }


package br.org.isbet.clinica.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorExcecoes {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> tratarErroValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest()
                .body(erros.stream()
                .map(DadosErroValidacao::new)
                .toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratarErroJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body("Erro de formatação JSON ou valor inválido para um dos campos.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErroRegraDeNegocio(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Falha na autenticação");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Acesso negado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErro500(Exception ex) {

        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor");
    }

    private record DadosErroValidacao(String campo, String mensagem) {

        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}

