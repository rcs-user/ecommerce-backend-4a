package br.edu.unifio.ecommerce.recursos;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.unifio.ecommerce.entidades.Pedido;
import br.edu.unifio.ecommerce.entidades.StatusPedido;
import br.edu.unifio.ecommerce.servicos.PedidoServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("pedidos")
public class PedidoRecurso {
    private final PedidoServico pedidos;

    public PedidoRecurso(PedidoServico pedidos) {
        this.pedidos = pedidos;
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidos.listar();
    }

    @GetMapping("{id}")
    public Pedido buscar(@PathVariable Integer id) {
        return pedidos.buscar(id);
    }

    @GetMapping("/consultas/cliente/{clienteId}")
    public List<Pedido> porCliente(@PathVariable Integer clienteId) {
        return pedidos.porCliente(clienteId);
    }

    @GetMapping("/consultas/status")
    public List<Pedido> porStatus(@RequestParam StatusPedido status) {
        return pedidos.porStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido criar(@Valid @RequestBody Pedido pedido) {
        return pedidos.criar(pedido);
    }

    @PatchMapping("{id}/status")
    public Pedido atualizarStatus(@PathVariable Integer id, @RequestParam StatusPedido status) {
        return pedidos.atualizarStatus(id, status);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        pedidos.excluir(id);
    }
}
