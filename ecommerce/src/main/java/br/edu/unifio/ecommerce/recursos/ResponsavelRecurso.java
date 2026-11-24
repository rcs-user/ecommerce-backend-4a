package br.edu.unifio.ecommerce.recursos;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.edu.unifio.ecommerce.entidades.Responsavel;
import br.edu.unifio.ecommerce.servicos.ResponsavelServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("responsaveis")
public class ResponsavelRecurso {
    private final ResponsavelServico responsaveis;
    public ResponsavelRecurso(ResponsavelServico responsaveis) { this.responsaveis = responsaveis; }

    @GetMapping
    public List<Responsavel> listar() { return responsaveis.listar(); }
    @GetMapping("{id}")
    public Responsavel buscar(@PathVariable Integer id) { return responsaveis.buscar(id); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Responsavel salvar(@Valid @RequestBody Responsavel responsavel) { return responsaveis.salvar(responsavel); }
    @PutMapping("{id}")
    public Responsavel atualizar(@PathVariable Integer id, @Valid @RequestBody Responsavel responsavel) {
        return responsaveis.atualizar(id, responsavel);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) { responsaveis.excluir(id); }
}
