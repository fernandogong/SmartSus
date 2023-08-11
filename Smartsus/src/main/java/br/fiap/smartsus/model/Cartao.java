package br.fiap.smartsus.model;

import org.hibernate.validator.constraints.CreditCardNumber;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Cartao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cartao_id")
  private Long Id;

  @CreditCardNumber(ignoreNonDigitCharacters = true)
  private String numeroCartao;

  @NotBlank
  private String nomeCartao;

  @NotNull
  @FutureOrPresent
  private String validadeCartao;

  @NotNull
  @Size(min = 3, max = 3)
  private Integer Cvv;
  
  @Pattern(regexp = "\"\\\\d{3}\\\\.\\\\d{3}\\\\.\\\\d{3}-\\\\d{2}\"")
  private String cpf;

  @ManyToOne(optional = true)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  
}
