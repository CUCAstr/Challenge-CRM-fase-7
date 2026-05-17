package br.com.savedra.challengebackend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Capturador Global de Exceções.
 * Garante que erros do backend sejam enviados como mensagens legíveis para o App Android.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Logamos o erro no terminal do backend
        System.err.println("Erro capturado: " + e.getMessage());
        
        // Enviamos apenas a mensagem simplificada para o frontend para evitar vazamento de stacktrace
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            message = "Erro interno no servidor.";
        }
        
        // Se for um erro de duplicidade no MongoDB
        if (message.contains("duplicate key error")) {
            message = "Este email já está cadastrado.";
        }

        return ResponseEntity.badRequest().body(message);
    }
}
