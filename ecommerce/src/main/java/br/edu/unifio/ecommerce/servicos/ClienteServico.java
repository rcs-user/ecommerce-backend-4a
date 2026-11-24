package br.edu.unifio.ecommerce.servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.ecommerce.entidades.Cliente;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.ClienteRepositorio;
import br.edu.unifio.ecommerce.repositorios.PedidoRepositorio;

@Service
public class ClienteServico {
    private final ClienteRepositorio clientes;
    private final PedidoRepositorio pedidos;

    public ClienteServico(ClienteRepositorio clientes, PedidoRepositorio pedidos) {
        this.clientes = clientes;
        this.pedidos = pedidos;
    }

    public List<Cliente> listar() {
        return clientes.findAll();
    }

    public Cliente buscar(Integer id) {
        return clientes.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " não encontrado"));
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        if (clientes.existsByEmailIgnoreCase(cliente.getEmail())) {
            throw new ConflitoDeNegocioException("Já existe um cliente cadastrado com este e-mail");
        }
        if (clientes.existsByDocumento(cliente.getDocumento())) {
            throw new ConflitoDeNegocioException("Já existe um cliente cadastrado com este documento");
        }
        return clientes.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Integer id, Cliente dados) {
        Cliente atual = buscar(id);
        if (clientes.existsByEmailIgnoreCaseAndIdNot(dados.getEmail(), id)) {
            throw new ConflitoDeNegocioException("Já existe um cliente cadastrado com este e-mail");
        }
        if (clientes.existsByDocumentoAndIdNot(dados.getDocumento(), id)) {
            throw new ConflitoDeNegocioException("Já existe um cliente cadastrado com este documento");
        }
        atual.setNome(dados.getNome());
        atual.setEmail(dados.getEmail());
        atual.setDocumento(dados.getDocumento());
        atual.setEndereco(dados.getEndereco());
        return clientes.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (pedidos.existsByClienteId(id)) {
            throw new ConflitoDeNegocioException("Não é possível excluir um cliente que possui pedidos");
        }
        clientes.deleteById(id);
    }
}
