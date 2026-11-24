package br.edu.unifio.ecommerce.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.unifio.ecommerce.entidades.ItemPedido;
import br.edu.unifio.ecommerce.servicos.ItemPedidoServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("itens-pedido")
public class ItemPedidoRecurso {
    private final ItemPedidoServico itens;

    public ItemPedidoRecurso(ItemPedidoServico itens) {
        this.itens = itens;
    }

    @GetMapping
    public List<ItemPedido> listar() {
        return itens.listar();
    }

    @GetMapping("{id}")
    public ItemPedido buscar(@PathVariable Integer id) {
        return itens.buscar(id);
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<ItemPedido> porPedido(@PathVariable Integer pedidoId) {
        return itens.porPedido(pedidoId);
    }

    @PostMapping("/pedido/{pedidoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemPedido salvar(@PathVariable Integer pedidoId, @Valid @RequestBody ItemPedido item) {
        return itens.salvar(pedidoId, item);
    }

    @PutMapping("{id}")
    public ItemPedido atualizar(@PathVariable Integer id, @Valid @RequestBody ItemPedido item) {
        return itens.atualizar(id, item);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        itens.excluir(id);
    }
}
