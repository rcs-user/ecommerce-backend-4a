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

import br.edu.unifio.ecommerce.entidades.Cliente;
import br.edu.unifio.ecommerce.servicos.ClienteServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("clientes")
public class ClienteRecurso {
    private final ClienteServico clientes;

    public ClienteRecurso(ClienteServico clientes) {
        this.clientes = clientes;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clientes.listar();
    }

    @GetMapping("{id}")
    public Cliente buscar(@PathVariable Integer id) {
        return clientes.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(@Valid @RequestBody Cliente cliente) {
        return clientes.salvar(cliente);
    }

    @PutMapping("{id}")
    public Cliente atualizar(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        return clientes.atualizar(id, cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        clientes.excluir(id);
    }
}
