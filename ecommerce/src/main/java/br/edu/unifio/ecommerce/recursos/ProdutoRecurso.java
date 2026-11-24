package br.edu.unifio.ecommerce.recursos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.unifio.ecommerce.entidades.Produto;
import br.edu.unifio.ecommerce.servicos.ProdutoServico;
import jakarta.validation.Valid;

@RestController 
@RequestMapping ("produtos")
@Validated
public class ProdutoRecurso {
    private final ProdutoServico produtos;

    public ProdutoRecurso(ProdutoServico produtos) {
        this.produtos = produtos;
    }

    @GetMapping 
    public List<Produto> listar () {
        return produtos.listar();
    }

    @GetMapping ("{id}")
    public Produto buscarPorId (@PathVariable Integer id) {
        return produtos.buscar(id);
    }

    @GetMapping("/consultas/categoria/{categoriaId}")
    public List<Produto> porCategoria(@PathVariable Short categoriaId) {
        return produtos.porCategoria(categoriaId);
    }

    @GetMapping("/consultas/preco")
    public List<Produto> porPreco(@RequestParam BigDecimal limite) {
        return produtos.porPreco(limite);
    }

    @GetMapping("/consultas/estoque-baixo")
    public List<Produto> estoqueBaixo(@RequestParam(defaultValue = "10") Short limite) {
        return produtos.estoqueBaixo(limite);
    }

    @PostMapping 
    @ResponseStatus (HttpStatus.CREATED)
    public Produto salvar (@Valid @RequestBody Produto produto) {
        return produtos.salvar(produto);
    }

    @PutMapping("{id}")
    public Produto atualizar(@PathVariable Integer id, @Valid @RequestBody Produto produto) {
        return produtos.atualizar(id, produto);
    }

    @DeleteMapping ("{id}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void excluirPorId (@PathVariable Integer id) {
        produtos.excluir(id);
    }
}
