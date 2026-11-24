package br.edu.unifio.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.ecommerce.entidades.ItemPedido;

public interface ItemPedidoRepositorio extends JpaRepository<ItemPedido, Integer> {
    List<ItemPedido> findByPedidoId(Integer pedidoId);
    boolean existsByProdutoId(Integer produtoId);
}
