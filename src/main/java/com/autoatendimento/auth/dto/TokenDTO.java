package com.autoatendimento.auth.dto;

import java.util.List;
import java.util.Map;

public class TokenDTO {
    // ... campos básicos (token, tipo, expiracaoEmSegundos, nomeCompleto)
    
    // NOVO: Lista de acessos Multi-Tenant (ex: empresas/perfis)
    private List<Map<String, Object>> acessos;

    public TokenDTO() {}

    // Construtor atualizado
    public TokenDTO(String token, String tipo, Long expiracaoEmSegundos, String nomeCompleto, List<Map<String, Object>> acessos) {
        // ... inicialização dos campos
        this.acessos = acessos;
    }

    // Getters e Setters
    public List<Map<String, Object>> getAcessos() { return acessos; }
    public void setAcessos(List<Map<String, Object>> acessos) { this.acessos = acessos; }
}
