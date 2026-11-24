package br.edu.unifio.ecommerce.recursos;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.edu.unifio.ecommerce.entidades.Motorista;
import br.edu.unifio.ecommerce.servicos.MotoristaServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("motoristas")
public class MotoristaRecurso {
    private final MotoristaServico motoristas;
    public MotoristaRecurso(MotoristaServico motoristas) { this.motoristas = motoristas; }

    @GetMapping
    public List<Motorista> listar() { return motoristas.listar(); }
    @GetMapping("{id}")
    public Motorista buscar(@PathVariable Integer id) { return motoristas.buscar(id); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Motorista salvar(@Valid @RequestBody Motorista motorista) { return motoristas.salvar(motorista); }
    @PutMapping("{id}")
    public Motorista atualizar(@PathVariable Integer id, @Valid @RequestBody Motorista motorista) {
        return motoristas.atualizar(id, motorista);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) { motoristas.excluir(id); }
}
