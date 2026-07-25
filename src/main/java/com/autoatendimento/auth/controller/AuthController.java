package com.autoatendimento.auth.controller;

import javax.crypto.SecretKey;

//import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autoatendimento.auth.dto.AlterarSenhaDTO;
import com.autoatendimento.auth.dto.CriarCredencialDTO;
import com.autoatendimento.auth.dto.LoginDTO;
import com.autoatendimento.auth.dto.RedefinirSenhaDTO;
import com.autoatendimento.auth.dto.TokenDTO;
import com.autoatendimento.auth.entity.Usuario;
import org.springframework.http.HttpStatus;
import com.autoatendimento.auth.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Value("${jwt.secret}")
	private String segredoEmTexto; // Injeta a String do application.properties

	private SecretKey chaveSecreta; // Armazena a chave pronta para o JJWT

	@PostConstruct
	public void init() {
	    // Transforma a String em uma SecretKey segura assim que o Spring inicializa a classe
	    this.chaveSecreta = Keys.hmacShaKeyFor(segredoEmTexto.getBytes(StandardCharsets.UTF_8));
	}
	
    private final AuthService service;
    
    public AuthController(AuthService service) { 
    	this.service = service; 
    }
    
    // Cria credencial para outros serviços
    @PostMapping("/criar-credencial")
    public ResponseEntity<Usuario> criarCredencial(@Valid @RequestBody CriarCredencialDTO dto) {
        Usuario usuarioCriado = service.criarCredencial(dto);
        return new ResponseEntity<>(usuarioCriado, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO dto) {
        TokenDTO token = service.autenticar(dto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/validar-token")
    public ResponseEntity<Boolean> validar(@RequestHeader("Authorization") String token) {
        boolean valido = service.validarToken(token);
        return ResponseEntity.ok(valido);
    }
    
    @PatchMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AlterarSenhaDTO dto) {
        // Extrai o login do token (método auxiliar abaixo)
        String login = extrairLoginDoToken(token);
        service.alterarSenha(login, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody RedefinirSenhaDTO dto) {
        String loginAdmin = extrairLoginDoToken(token);
        service.redefinirSenhaPerdida(loginAdmin, dto);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para extrair login do token
    private String extrairLoginDoToken(String tokenCompleto) {
        String token = tokenCompleto.replace("Bearer ", "");
        return Jwts.parser()
                .verifyWith(chaveSecreta)       // Substitui o antigo .setSigningKey()
                .build()                        // Constrói o parser de forma segura e imutável
                .parseSignedClaims(token)       // Substitui o antigo .parseClaimsJws()
                .getPayload()                   // Substitui o antigo .getBody()
                .getSubject();
    }
}