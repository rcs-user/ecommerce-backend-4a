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

import br.edu.unifio.ecommerce.entidades.Categoria;
import br.edu.unifio.ecommerce.servicos.CategoriaServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("categorias")
public class CategoriaRecurso {
    private final CategoriaServico categorias;

    public CategoriaRecurso(CategoriaServico categorias) {
        this.categorias = categorias;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categorias.listar();
    }

    @GetMapping("{id}")
    public Categoria buscar(@PathVariable Integer id) {
        return categorias.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria salvar(@Valid @RequestBody Categoria categoria) {
        return categorias.salvar(categoria);
    }

    @PutMapping("{id}")
    public Categoria atualizar(@PathVariable Short id, @Valid @RequestBody Categoria categoria) {
        return categorias.atualizar(id, categoria);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Short id) {
        categorias.excluir(id);
    }
}
