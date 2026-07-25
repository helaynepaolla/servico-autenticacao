package com.autoatendimento.auth.config;

import com.autoatendimento.auth.entity.PerfilUsuario;
import com.autoatendimento.auth.entity.Usuario;
import com.autoatendimento.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInicial implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioInicial(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe usuário admin
        if (repository.findByLogin("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            // Senha: 123456 — já criptografada automaticamente
            admin.setSenha(encoder.encode("123456"));
            admin.setPerfil(PerfilUsuario.ADMIN);
            admin.setNomeCompleto("Administrador do Sistema");

            repository.save(admin);
            System.out.println("✅ Usuário ADMIN criado com sucesso! Login: admin | Senha: 123456");
        } else {
            System.out.println("ℹ️ Usuário ADMIN já existe, pulando criação.");
        }
    }
}