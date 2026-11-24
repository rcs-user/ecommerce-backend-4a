package br.edu.unifio.ecommerce.recursos;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.edu.unifio.ecommerce.entidades.Veiculo;
import br.edu.unifio.ecommerce.servicos.VeiculoServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("veiculos")
public class VeiculoRecurso {
    private final VeiculoServico veiculos;
    public VeiculoRecurso(VeiculoServico veiculos) { this.veiculos = veiculos; }

    @GetMapping
    public List<Veiculo> listar() { return veiculos.listar(); }
    @GetMapping("{id}")
    public Veiculo buscar(@PathVariable Integer id) { return veiculos.buscar(id); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Veiculo salvar(@Valid @RequestBody Veiculo veiculo) { return veiculos.salvar(veiculo); }
    @PutMapping("{id}")
    public Veiculo atualizar(@PathVariable Integer id, @Valid @RequestBody Veiculo veiculo) {
        return veiculos.atualizar(id, veiculo);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) { veiculos.excluir(id); }
}
