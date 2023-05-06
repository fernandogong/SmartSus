package br.fiap.smartsus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.fiap.smartsus.model.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long>{
}
