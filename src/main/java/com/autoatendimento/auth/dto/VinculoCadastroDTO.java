package com.autoatendimento.auth.dto;

import com.autoatendimento.auth.entity.PerfilUsuario;
import jakarta.validation.constraints.NotNull;

public class VinculoCadastroDTO {

    @NotNull private Long usuarioId;
    @NotNull private Long empresaId;
    @NotNull private PerfilUsuario perfil; // ADMIN ou FUNCIONARIO

    // Getters e Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
}
