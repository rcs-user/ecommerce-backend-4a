package br.edu.unifio.ecommerce.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Responsavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 120)
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false)
    private String telefone;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;
}
