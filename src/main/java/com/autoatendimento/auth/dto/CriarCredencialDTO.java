package com.autoatendimento.auth.dto;

import com.autoatendimento.auth.entity.PerfilUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public class CriarCredencialDTO {
    
    @NotBlank(message = "Login/Usuário é obrigatório")
    private String login;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil; // FUNCIONARIO, ADMIN, CLIENTE
    
    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    // Getters e Setters
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }    
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
	public PerfilUsuario getPerfil() {
		return perfil;
	}
	public void setPerfil(PerfilUsuario perfil) {
		this.perfil = perfil;
	}
    
}