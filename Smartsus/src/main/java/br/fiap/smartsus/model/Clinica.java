package br.fiap.smartsus.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Clinica {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "clinica_id")
  private Long Id;

  @NotBlank
  private String nome;

  @NotBlank
  private String especialidade;

  @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
  private String cnpj;
  
  @NotBlank
  @Positive
  private Long preco;

  @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
  private List<Agendamento> agendamentos = new ArrayList<>();


}
