package br.edu.unifio.ecommerce.servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.ecommerce.entidades.ItemPedido;
import br.edu.unifio.ecommerce.entidades.StatusPedido;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.ItemPedidoRepositorio;
import br.edu.unifio.ecommerce.repositorios.PedidoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ProdutoRepositorio;

@Service
public class ItemPedidoServico {
    private final ItemPedidoRepositorio itens;
    private final PedidoRepositorio pedidos;
    private final ProdutoRepositorio produtos;
    private final PedidoServico pedidoServico;

    public ItemPedidoServico(ItemPedidoRepositorio itens, PedidoRepositorio pedidos,
                             ProdutoRepositorio produtos, PedidoServico pedidoServico) {
        this.itens = itens;
        this.pedidos = pedidos;
        this.produtos = produtos;
        this.pedidoServico = pedidoServico;
    }

    public List<ItemPedido> listar() {
        return itens.findAll();
    }

    public ItemPedido buscar(Integer id) {
        return itens.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item do pedido " + id + " não encontrado"));
    }

    public List<ItemPedido> porPedido(Integer pedidoId) {
        return itens.findByPedidoId(pedidoId);
    }

    @Transactional
    public ItemPedido salvar(Integer pedidoId, ItemPedido item) {
        var pedido = pedidos.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido " + pedidoId + " não encontrado"));
        validarPedidoEditavel(pedido);
        if (item.getProduto() == null || item.getProduto().getId() == null) {
            throw new ConflitoDeNegocioException("Informe o produto do item");
        }
        var produto = produtos.findById(item.getProduto().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto informado não encontrado"));
        short quantidade = item.getQuantidade() == null ? 0 : item.getQuantidade();
        if (quantidade < 1 || produto.getEstoque() < quantidade) {
            throw new ConflitoDeNegocioException("Quantidade inválida ou estoque insuficiente");
        }
        produto.setEstoque((short) (produto.getEstoque() - quantidade));
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setPrecoUnitario(produto.getPreco());
        pedido.getItens().add(item);
        pedidoServico.recalcular(pedido);
        return itens.save(item);
    }

    @Transactional
    public ItemPedido atualizar(Integer id, ItemPedido dados) {
        ItemPedido atual = buscar(id);
        validarPedidoEditavel(atual.getPedido());
        if (dados.getQuantidade() == null) {
            throw new ConflitoDeNegocioException("Informe a quantidade do item");
        }
        if (dados.getQuantidade() < 1) {
            throw new ConflitoDeNegocioException("A quantidade do item deve ser maior que zero");
        }
        short diferenca = (short) (dados.getQuantidade() - atual.getQuantidade());
        var produto = atual.getProduto();
        if (diferenca > 0 && produto.getEstoque() < diferenca) {
            throw new ConflitoDeNegocioException("Estoque insuficiente");
        }
        produto.setEstoque((short) (produto.getEstoque() - diferenca));
        atual.setQuantidade(dados.getQuantidade());
        pedidoServico.recalcular(atual.getPedido());
        return itens.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        ItemPedido item = buscar(id);
        var pedido = item.getPedido();
        validarPedidoEditavel(pedido);
        if (pedido.getStatus() != StatusPedido.CANCELADO) {
            item.getProduto().setEstoque((short) (item.getProduto().getEstoque() + item.getQuantidade()));
        }
        pedido.getItens().remove(item);
        itens.delete(item);
        pedidoServico.recalcular(pedido);
    }

    private void validarPedidoEditavel(br.edu.unifio.ecommerce.entidades.Pedido pedido) {
        if (pedido.getStatus() == StatusPedido.CANCELADO
                || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new ConflitoDeNegocioException(
                    "Não é possível alterar itens de um pedido cancelado ou entregue");
        }
    }
}
