package com.autoatendimento.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vinculos_usuario_empresa")
public class VinculoUsuarioEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId; // Vincula ao ID da empresa do Marketplace

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil; // ADMIN ou FUNCIONARIO

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
}
