package br.edu.unifio.ecommerce.entidades;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Pedido pedido;

    @NotNull
    @ManyToOne(optional = false)
    private Produto produto;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Short quantidade;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;
}
