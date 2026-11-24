package br.edu.unifio.ecommerce.recursos;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.edu.unifio.ecommerce.entidades.Aluno;
import br.edu.unifio.ecommerce.servicos.AlunoServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("alunos")
public class AlunoRecurso {
    private final AlunoServico alunos;
    public AlunoRecurso(AlunoServico alunos) { this.alunos = alunos; }

    @GetMapping
    public List<Aluno> listar() { return alunos.listar(); }
    @GetMapping("{id}")
    public Aluno buscar(@PathVariable Integer id) { return alunos.buscar(id); }
    @GetMapping("/consultas/responsavel/{responsavelId}")
    public List<Aluno> porResponsavel(@PathVariable Integer responsavelId) {
        return alunos.porResponsavel(responsavelId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Aluno salvar(@Valid @RequestBody Aluno aluno) { return alunos.salvar(aluno); }
    @PutMapping("{id}")
    public Aluno atualizar(@PathVariable Integer id, @Valid @RequestBody Aluno aluno) {
        return alunos.atualizar(id, aluno);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) { alunos.excluir(id); }
}
