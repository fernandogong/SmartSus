package br.fiap.smartsus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.fiap.smartsus.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
}
