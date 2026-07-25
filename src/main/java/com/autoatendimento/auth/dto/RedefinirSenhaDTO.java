package com.autoatendimento.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaDTO {
    @NotBlank(message = "Login é obrigatório")
    private String login;

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "A nova senha deve ter pelo menos 6 caracteres")
    private String novaSenha;

    @NotBlank(message = "Confirmação da senha é obrigatória")
    private String confirmarNovaSenha;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmarNovaSenha() {
		return confirmarNovaSenha;
	}

	public void setConfirmarNovaSenha(String confirmarNovaSenha) {
		this.confirmarNovaSenha = confirmarNovaSenha;
	}
    
    
    
}