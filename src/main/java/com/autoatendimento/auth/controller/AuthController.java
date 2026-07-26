package com.autoatendimento.auth.controller;

import com.autoatendimento.auth.dto.*;
import com.autoatendimento.auth.entity.Usuario;
import com.autoatendimento.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto) {
        // Agora retorna o token contendo os múltiplos vínculos de empresas
        TokenDTO token = service.autenticar(dto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid CriarCredencialDTO dto) {
        // Registra o usuário global com seu CPF obrigatório
        Usuario usuario = service.criarCredencial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validar(@RequestParam String token) {
        boolean valido = service.validarToken(token);
        return ResponseEntity.ok(valido);
    }
    
    @PostMapping("/vinculos")
    public ResponseEntity<Void> criarVinculo(@RequestBody @Valid VinculoCadastroDTO dto) {
        service.criarVinculo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
