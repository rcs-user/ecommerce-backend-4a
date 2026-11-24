package br.edu.unifio.ecommerce.servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.unifio.ecommerce.entidades.Categoria;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.CategoriaRepositorio;
import br.edu.unifio.ecommerce.repositorios.ProdutoRepositorio;

@Service
public class CategoriaServico {
    private final CategoriaRepositorio categorias;
    private final ProdutoRepositorio produtos;

    public CategoriaServico(CategoriaRepositorio categorias, ProdutoRepositorio produtos) {
        this.categorias = categorias;
        this.produtos = produtos;
    }

    public List<Categoria> listar() {
        return categorias.findAll();
    }

    public Categoria buscar(Integer id) {
        return categorias.findById(id.shortValue())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + id + " não encontrada"));
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        if (categorias.existsByNomeIgnoreCase(categoria.getNome())) {
            throw new ConflitoDeNegocioException("Já existe uma categoria com este nome");
        }
        return categorias.save(categoria);
    }

    @Transactional
    public Categoria atualizar(Short id, Categoria dados) {
        Categoria atual = buscar(id.intValue());
        if (categorias.existsByNomeIgnoreCaseAndIdNot(dados.getNome(), id)) {
            throw new ConflitoDeNegocioException("Já existe uma categoria com este nome");
        }
        atual.setNome(dados.getNome());
        atual.setDescricao(dados.getDescricao());
        return categorias.save(atual);
    }

    @Transactional
    public void excluir(Short id) {
        buscar(id.intValue());
        if (!produtos.findByCategoriaId(id).isEmpty()) {
            throw new ConflitoDeNegocioException("Não é possível excluir uma categoria que possui produtos");
        }
        categorias.deleteById(id);
    }
}
