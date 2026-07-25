package com.autoatendimento.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.autoatendimento.auth.dto.AlterarSenhaDTO;
import com.autoatendimento.auth.dto.CriarCredencialDTO;
import com.autoatendimento.auth.dto.LoginDTO;
import com.autoatendimento.auth.dto.RedefinirSenhaDTO;
import com.autoatendimento.auth.dto.TokenDTO;
import com.autoatendimento.auth.entity.Usuario;
import com.autoatendimento.auth.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

@Service
public class AuthService {

	@Value("${jwt.secret}")
	private String segredoEmTexto; // Injeta a String do application.properties

	private SecretKey chaveSecreta; // Armazena a chave pronta para o JJWT

	@PostConstruct
	public void init() {
	    // Transforma a String em uma SecretKey segura assim que o Spring inicializa a classe
	    this.chaveSecreta = Keys.hmacShaKeyFor(segredoEmTexto.getBytes(StandardCharsets.UTF_8));
	}

    @Value("${jwt.expiracao-segundos}")
    private Long expiracaoSegundos;

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Usuario criarCredencial(CriarCredencialDTO dto) {
        Usuario u = new Usuario();
        u.setLogin(dto.getLogin());
        u.setSenha(encoder.encode(dto.getSenha()));
        u.setPerfil(dto.getPerfil());
        u.setNomeCompleto(dto.getNomeCompleto());
        return repo.save(u);
    }    
  
    public TokenDTO autenticar(LoginDTO dto) {
        // Busca o usuário. Se for admin (criado em lowercase na inicialização), ele vai achar
        Usuario u = repo.findByLogin(dto.getLogin().toLowerCase()) 
            .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Login ou senha inválidos"));

        // Valida o hash Bcrypt
        if (!encoder.matches(dto.getSenha(), u.getSenha())) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("perfil", u.getPerfil());
        claims.put("nomeCompleto", u.getNomeCompleto());

        String token = Jwts.builder()
            .subject(u.getLogin())
            .claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + (expiracaoSegundos * 1000)))
            .signWith(chaveSecreta)
            .compact();

        return new TokenDTO(token, "Bearer", expiracaoSegundos, u.getPerfil().name(), u.getNomeCompleto());
    }


    public boolean validarToken(String tokenCompleto) {
    	try {
            String token = tokenCompleto.replace("Bearer ", "");
            
            Jwts.parser()
                .verifyWith(chaveSecreta)       // Substitui o antigo .setSigningKey()
                .build()                        // Constrói o parser
                .parseSignedClaims(token);      // Substitui o antigo .parseClaimsJws()
                
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void alterarSenha(String login, AlterarSenhaDTO dto) {
        Usuario u = repo.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!encoder.matches(dto.getSenhaAtual(), u.getSenha())) {
        	 //Lançar com o status UNAUTHORIZED (401) ou BAD_REQUEST (400)
        	throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }

        u.setSenha(encoder.encode(dto.getNovaSenha()));
        repo.save(u);
    }

    public void redefinirSenhaPerdida(String loginAdmin, RedefinirSenhaDTO dto) {
        Usuario u = repo.findByLogin(loginAdmin)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!dto.getNovaSenha().equals(dto.getConfirmarNovaSenha())) {
            throw new RuntimeException("As senhas não coincidem");
        }

        u.setSenha(encoder.encode(dto.getNovaSenha()));
        repo.save(u);
    }
}