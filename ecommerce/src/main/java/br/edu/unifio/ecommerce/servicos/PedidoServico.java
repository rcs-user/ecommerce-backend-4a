package br.edu.unifio.ecommerce.servicos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.ecommerce.entidades.ItemPedido;
import br.edu.unifio.ecommerce.entidades.Pedido;
import br.edu.unifio.ecommerce.entidades.StatusPedido;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.ClienteRepositorio;
import br.edu.unifio.ecommerce.repositorios.PedidoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ProdutoRepositorio;

@Service
public class PedidoServico {
    private final PedidoRepositorio pedidos;
    private final ClienteRepositorio clientes;
    private final ProdutoRepositorio produtos;

    public PedidoServico(PedidoRepositorio pedidos, ClienteRepositorio clientes, ProdutoRepositorio produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    public List<Pedido> listar() {
        return pedidos.findAll();
    }

    public Pedido buscar(Integer id) {
        return pedidos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido " + id + " não encontrado"));
    }

    public List<Pedido> porCliente(Integer clienteId) {
        return pedidos.findByClienteId(clienteId);
    }

    public List<Pedido> porStatus(StatusPedido status) {
        return pedidos.findByStatus(status);
    }

    @Transactional
    public Pedido criar(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new ConflitoDeNegocioException("Informe um cliente válido para o pedido");
        }
        pedido.setCliente(clientes.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente informado não encontrado")));
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new ConflitoDeNegocioException("O pedido deve possuir pelo menos um item");
        }

        pedido.setStatus(StatusPedido.NOVO);
        pedido.setTotal(BigDecimal.ZERO);
        for (ItemPedido item : pedido.getItens()) {
            if (item.getProduto() == null || item.getProduto().getId() == null) {
                throw new ConflitoDeNegocioException("Todo item deve informar um produto");
            }
            var produto = produtos.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto " + item.getProduto().getId() + " não encontrado"));
            short quantidade = item.getQuantidade() == null ? 0 : item.getQuantidade();
            if (quantidade < 1) {
                throw new ConflitoDeNegocioException("A quantidade do item deve ser maior que zero");
            }
            if (produto.getEstoque() < quantidade) {
                throw new ConflitoDeNegocioException(
                        "Estoque insuficiente para o produto " + produto.getNome());
            }
            produto.setEstoque((short) (produto.getEstoque() - quantidade));
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
            item.setPedido(pedido);
            pedido.setTotal(pedido.getTotal().add(produto.getPreco().multiply(BigDecimal.valueOf(quantidade))));
        }
        return pedidos.save(pedido);
    }

    @Transactional
    public Pedido atualizarStatus(Integer id, StatusPedido status) {
        Pedido pedido = buscar(id);
        if (status == null) {
            throw new ConflitoDeNegocioException("Informe um status válido");
        }

        StatusPedido atual = pedido.getStatus();
        validarTransicao(atual, status);

        if (status == StatusPedido.CANCELADO) {
            for (ItemPedido item : pedido.getItens()) {
                item.getProduto().setEstoque(
                        (short) (item.getProduto().getEstoque() + item.getQuantidade()));
            }
        }

        pedido.setStatus(status);
        return pedidos.save(pedido);
    }

    @Transactional
    public void excluir(Integer id) {
        Pedido pedido = buscar(id);
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new ConflitoDeNegocioException("Não é possível excluir um pedido já entregue");
        }
        if (pedido.getStatus() != StatusPedido.CANCELADO) {
            for (ItemPedido item : pedido.getItens()) {
                item.getProduto().setEstoque(
                        (short) (item.getProduto().getEstoque() + item.getQuantidade()));
            }
        }
        pedidos.delete(pedido);
    }

    @Transactional
    public Pedido recalcular(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : pedido.getItens()) {
            total = total.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        pedido.setTotal(total);
        return pedidos.save(pedido);
    }

    private void validarTransicao(StatusPedido atual, StatusPedido novo) {
        if (atual == novo) {
            return;
        }
        boolean valida = switch (atual) {
            case NOVO -> novo == StatusPedido.PAGO || novo == StatusPedido.CANCELADO;
            case PAGO -> novo == StatusPedido.ENVIADO || novo == StatusPedido.CANCELADO;
            case ENVIADO -> novo == StatusPedido.ENTREGUE || novo == StatusPedido.CANCELADO;
            case ENTREGUE -> false;
            case CANCELADO -> false;
        };
        if (!valida) {
            throw new ConflitoDeNegocioException(
                    "Não é possível alterar o status de " + atual + " para " + novo);
        }
    }
}
