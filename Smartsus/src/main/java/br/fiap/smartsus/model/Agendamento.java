package br.fiap.smartsus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Agendamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "agendamento_id")
  private Long Id;

  @NotBlank
  @FutureOrPresent
  private LocalDate AgendamentoData;

  @ManyToOne(optional = true)
  @JoinColumn(name = "usuario_id")
  private Usuario usuarios;

  @ManyToOne(optional = true)
  @JoinColumn(name = "clinica_id")
  private Clinica clinica;
  

  
}
