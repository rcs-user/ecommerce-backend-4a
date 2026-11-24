package br.edu.unifio.ecommerce.entidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 120)
    @Column(nullable = false)
    private String nome;

    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    @NotNull
    @Column(nullable = false)
    private LocalTime horarioSaida;

    @NotNull
    @Column(nullable = false)
    private LocalTime horarioChegada;

    @NotNull
    @ManyToOne(optional = false)
    private Motorista motorista;

    @NotNull
    @ManyToOne(optional = false)
    private Veiculo veiculo;

    @ManyToMany
    @JoinTable(
            name = "viagem_aluno",
            joinColumns = @JoinColumn(name = "viagem_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id"))
    private List<Aluno> alunos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusViagem status = StatusViagem.PLANEJADA;

    @ElementCollection
    @CollectionTable(name = "viagem_presenca", joinColumns = @JoinColumn(name = "viagem_id"))
    @MapKeyColumn(name = "aluno_id")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Map<Integer, StatusPresenca> presenca = new java.util.HashMap<>();
}
