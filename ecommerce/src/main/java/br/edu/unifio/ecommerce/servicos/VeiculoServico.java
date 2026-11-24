package br.edu.unifio.ecommerce.servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.unifio.ecommerce.entidades.Veiculo;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.VeiculoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ViagemRepositorio;

@Service
public class VeiculoServico {
    private final VeiculoRepositorio veiculos;
    private final ViagemRepositorio viagens;

    public VeiculoServico(VeiculoRepositorio veiculos, ViagemRepositorio viagens) {
        this.veiculos = veiculos;
        this.viagens = viagens;
    }

    public List<Veiculo> listar() {
        return veiculos.findAll();
    }

    public Veiculo buscar(Integer id) {
        return veiculos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo " + id + " não encontrado"));
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo) {
        if (veiculos.existsByPlacaIgnoreCase(veiculo.getPlaca())) {
            throw new ConflitoDeNegocioException("Já existe um veículo com esta placa");
        }
        return veiculos.save(veiculo);
    }

    @Transactional
    public Veiculo atualizar(Integer id, Veiculo dados) {
        Veiculo atual = buscar(id);
        if (veiculos.existsByPlacaIgnoreCaseAndIdNot(dados.getPlaca(), id)) {
            throw new ConflitoDeNegocioException("Já existe um veículo com esta placa");
        }
        for (var viagem : viagens.findByVeiculoId(id)) {
            if (dados.getCapacidade() < viagem.getAlunos().size()) {
                throw new ConflitoDeNegocioException(
                        "A capacidade não pode ser menor que os alunos da viagem " + viagem.getNome());
            }
        }
        atual.setPlaca(dados.getPlaca());
        atual.setModelo(dados.getModelo());
        atual.setCapacidade(dados.getCapacidade());
        return veiculos.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (viagens.existsByVeiculoId(id)) {
            throw new ConflitoDeNegocioException("Não é possível excluir um veículo vinculado a uma viagem");
        }
        veiculos.deleteById(id);
    }
}
