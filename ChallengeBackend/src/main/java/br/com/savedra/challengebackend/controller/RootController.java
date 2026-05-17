package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    /**
     * Endpoint raiz para verificação rápida de status do servidor.
     * Retorna uma mensagem simples para confirmar que o backend está online.
     */
    @GetMapping("/")
    public String index() {
        return "Backend is running!";
    }
}
