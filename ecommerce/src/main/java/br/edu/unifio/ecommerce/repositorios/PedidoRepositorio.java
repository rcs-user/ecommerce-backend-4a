package br.edu.unifio.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.ecommerce.entidades.Pedido;
import br.edu.unifio.ecommerce.entidades.StatusPedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByClienteId(Integer clienteId);
    List<Pedido> findByStatus(StatusPedido status);
    boolean existsByClienteId(Integer clienteId);
}
