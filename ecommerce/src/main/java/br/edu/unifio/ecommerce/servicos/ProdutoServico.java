package br.edu.unifio.ecommerce.servicos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.ecommerce.entidades.Produto;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.ProdutoRepositorio;
import br.edu.unifio.ecommerce.repositorios.CategoriaRepositorio;
import br.edu.unifio.ecommerce.repositorios.ItemPedidoRepositorio;

@Service
public class ProdutoServico {
    private final ProdutoRepositorio produtos;
    private final CategoriaRepositorio categorias;
    private final ItemPedidoRepositorio itens;

    public ProdutoServico(ProdutoRepositorio produtos, CategoriaRepositorio categorias,
                          ItemPedidoRepositorio itens) {
        this.produtos = produtos;
        this.categorias = categorias;
        this.itens = itens;
    }

    public List<Produto> listar() {
        return produtos.findAll();
    }

    public Produto buscar(Integer id) {
        return produtos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto " + id + " não encontrado"));
    }

    public List<Produto> porCategoria(Short categoriaId) {
        return produtos.findByCategoriaId(categoriaId);
    }

    public List<Produto> porPreco(BigDecimal preco) {
        return produtos.findByPrecoLessThanEqual(preco);
    }

    public List<Produto> estoqueBaixo(Short limite) {
        return produtos.buscarComEstoqueAbaixoDe(limite);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validarPrecoEstoque(produto);
        validarCategoria(produto);
        if (produtos.existsByNomeIgnoreCase(produto.getNome())) {
            throw new ConflitoDeNegocioException("Já existe um produto com este nome");
        }
        return produtos.save(produto);
    }

    @Transactional
    public Produto atualizar(Integer id, Produto dados) {
        Produto atual = buscar(id);
        if (produtos.existsByNomeIgnoreCaseAndIdNot(dados.getNome(), id)) {
            throw new ConflitoDeNegocioException("Já existe um produto com este nome");
        }
        atual.setNome(dados.getNome());
        atual.setDescricao(dados.getDescricao());
        atual.setPreco(dados.getPreco());
        atual.setEstoque(dados.getEstoque());
        atual.setCategoria(dados.getCategoria());
        validarPrecoEstoque(atual);
        validarCategoria(atual);
        return produtos.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (itens.existsByProdutoId(id)) {
            throw new ConflitoDeNegocioException("Não é possível excluir um produto que possui itens em pedidos");
        }
        produtos.deleteById(id);
    }

    private void validarPrecoEstoque(Produto produto) {
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflitoDeNegocioException("O preço do produto deve ser maior que zero");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0) {
            throw new ConflitoDeNegocioException("O estoque não pode ser negativo");
        }
    }

    private void validarCategoria(Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new ConflitoDeNegocioException("Informe uma categoria válida");
        }
        produto.setCategoria(categorias.findById(produto.getCategoria().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria informada não encontrada")));
    }
}
