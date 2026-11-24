package br.edu.unifio.ecommerce.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, unique = true)
    private String placa;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String modelo;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer capacidade;
}
