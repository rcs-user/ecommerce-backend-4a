package br.edu.unifio.ecommerce.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Short id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, unique = true)
    private String nome;

    @Size(max = 255)
    private String descricao;
}
