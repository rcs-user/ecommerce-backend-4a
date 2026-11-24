package br.edu.unifio.ecommerce.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 120)
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String escola;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false)
    private String turma;

    @NotNull
    @ManyToOne(optional = false)
    private Responsavel responsavel;
}
