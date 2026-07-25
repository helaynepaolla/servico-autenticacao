package com.autoatendimento.auth.dto;

public class TokenDTO {

    private String token;
    private String tipo;
    private Long expiracaoEmSegundos;
    private String perfil;
    private String nomeCompleto;

    // Construtor VAZIO (obrigatório para serialização)
    public TokenDTO() {}

    // ✅ Construtor com os 5 campos na ordem correta
    public TokenDTO(String token, String tipo, Long expiracaoEmSegundos, String perfil, String nomeCompleto) {
        this.token = token;
        this.tipo = tipo;
        this.expiracaoEmSegundos = expiracaoEmSegundos;
        this.perfil = perfil;
        this.nomeCompleto = nomeCompleto;
    }

    // Getters e Setters (confira todos existem)
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getExpiracaoEmSegundos() { return expiracaoEmSegundos; }
    public void setExpiracaoEmSegundos(Long expiracaoEmSegundos) { this.expiracaoEmSegundos = expiracaoEmSegundos; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
}