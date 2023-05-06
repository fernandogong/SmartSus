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
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "usuario_id")
  private Long Id;

  @Email
  private String email;

  @Size(min = 8, max = 20)
  private String senha;
  
  @NotBlank
  private String celular;
  
  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
  private List<Cartao> cartoes = new ArrayList<>();
}
