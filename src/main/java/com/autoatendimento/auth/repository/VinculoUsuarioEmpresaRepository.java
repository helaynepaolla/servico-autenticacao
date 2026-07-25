package com.autoatendimento.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoatendimento.auth.entity.VinculoUsuarioEmpresa;

public interface VinculoUsuarioEmpresaRepository extends JpaRepository<VinculoUsuarioEmpresa, Long>{
	 // Busca todas as empresas que um usuário possui acesso
    List<VinculoUsuarioEmpresa> findByUsuarioId(Long usuarioId);
}
