package br.edu.unifio.ecommerce.recursos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.edu.unifio.ecommerce.entidades.StatusPresenca;
import br.edu.unifio.ecommerce.entidades.StatusViagem;
import br.edu.unifio.ecommerce.entidades.Viagem;
import br.edu.unifio.ecommerce.servicos.ViagemServico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("viagens")
public class ViagemRecurso {
    private final ViagemServico viagens;
    public ViagemRecurso(ViagemServico viagens) { this.viagens = viagens; }

    @GetMapping
    public List<Viagem> listar() { return viagens.listar(); }
    @GetMapping("{id}")
    public Viagem buscar(@PathVariable Integer id) { return viagens.buscar(id); }
    @GetMapping("/consultas/status")
    public List<Viagem> porStatus(@RequestParam StatusViagem status) { return viagens.porStatus(status); }
    @GetMapping("/consultas/data")
    public List<Viagem> porData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return viagens.porData(data);
    }
    @GetMapping("/consultas/motorista/{motoristaId}")
    public List<Viagem> porMotorista(@PathVariable Integer motoristaId) {
        return viagens.porMotorista(motoristaId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viagem salvar(@Valid @RequestBody Viagem viagem) { return viagens.salvar(viagem); }
    @PutMapping("{id}")
    public Viagem atualizar(@PathVariable Integer id, @Valid @RequestBody Viagem viagem) {
        return viagens.atualizar(id, viagem);
    }
    @PatchMapping("{id}/status")
    public Viagem atualizarStatus(@PathVariable Integer id, @RequestParam StatusViagem status) {
        return viagens.atualizarStatus(id, status);
    }
    @PatchMapping("{id}/presenca/{alunoId}")
    public Viagem atualizarPresenca(@PathVariable Integer id, @PathVariable Integer alunoId,
                                    @RequestParam StatusPresenca status) {
        return viagens.atualizarPresenca(id, alunoId, status);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) { viagens.excluir(id); }
}
