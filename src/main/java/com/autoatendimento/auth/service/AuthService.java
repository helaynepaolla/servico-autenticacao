package com.autoatendimento.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.autoatendimento.auth.dto.CriarCredencialDTO;
import com.autoatendimento.auth.dto.LoginDTO;
import com.autoatendimento.auth.dto.TokenDTO;
import com.autoatendimento.auth.entity.Usuario;
import com.autoatendimento.auth.entity.VinculoUsuarioEmpresa;
import com.autoatendimento.auth.repository.UsuarioRepository;
import com.autoatendimento.auth.repository.VinculoUsuarioEmpresaRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class AuthService {
    //(configurações JWT)
	@Value("${jwt.secret}")
	private String segredoEmTexto; // Injeta a String do application.properties

	private SecretKey chaveSecreta; // Armazena a chave pronta para o JJWT

	@Value("${jwt.expiracao-segundos}")
	private Long expiracaoSegundos;
	
	@PostConstruct
    public void init() {
        // Transforma a String em uma SecretKey segura assim que o Spring inicializa a classe
        this.chaveSecreta = Keys.hmacShaKeyFor(segredoEmTexto.getBytes(StandardCharsets.UTF_8));
    }
    
    private final UsuarioRepository repo;
    private final VinculoUsuarioEmpresaRepository vinculoRepo;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepository repo, VinculoUsuarioEmpresaRepository vinculoRepo, PasswordEncoder encoder) {
        this.repo = repo;
        this.vinculoRepo = vinculoRepo;
        this.encoder = encoder;
    }   
    

    // --- MÉTODOS REESCRITOS (Ajustado para CPF único e Vínculos) ---

    public Usuario criarCredencial(CriarCredencialDTO dto) {
        // Validação de CPF e Login únicos
        if(repo.findByCpf(dto.getCpf()).isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
        if(repo.findByLogin(dto.getLogin().toLowerCase()).isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");

        Usuario u = new Usuario();
        u.setLogin(dto.getLogin().toLowerCase());
        u.setSenha(encoder.encode(dto.getSenha()));
        u.setNomeCompleto(dto.getNomeCompleto());
        u.setCpf(dto.getCpf());
        return repo.save(u);
    }

    public TokenDTO autenticar(LoginDTO dto) {
        // 1. Busca o usuário pelo login único global
        Usuario u = repo.findByLogin(dto.getLogin().toLowerCase())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Login ou senha inválidos"));

        // 2. Valida a senha criptografada com Bcrypt
        if (!encoder.matches(dto.getSenha(), u.getSenha())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
        }

        // 3. Busca os vínculos multi-tenant passando o ID do usuário (u.getId())
        List<VinculoUsuarioEmpresa> vinculos = vinculoRepo.findByUsuarioId(u.getId());
        List<Map<String, Object>> acessos = vinculos.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("empresaId", v.getEmpresaId());
            map.put("perfil", v.getPerfil().name());
            return map;
        }).collect(Collectors.toList());

        // 4. Monta as informações (claims) que vão dentro do token JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("nomeCompleto", u.getNomeCompleto());
        claims.put("cpf", u.getCpf());
        claims.put("acessos", acessos);

        // 5. Gera o token JWT estruturado
        String token = Jwts.builder()
                .subject(u.getLogin())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (expiracaoSegundos * 1000)))
                .signWith(chaveSecreta)
                .compact();

        // 6. Retorna o DTO com a nova lista de acessos mapeada
        return new TokenDTO(token, "Bearer", expiracaoSegundos, u.getNomeCompleto(), acessos);
    }
    
    public boolean validarToken(String tokenCompleto) {
        try {
            String token = tokenCompleto.replace("Bearer ", "");
            Jwts.parser()
                .verifyWith(chaveSecreta)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
