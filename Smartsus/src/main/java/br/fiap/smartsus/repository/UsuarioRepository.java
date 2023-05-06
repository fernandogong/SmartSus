package br.fiap.smartsus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.fiap.smartsus.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
}
