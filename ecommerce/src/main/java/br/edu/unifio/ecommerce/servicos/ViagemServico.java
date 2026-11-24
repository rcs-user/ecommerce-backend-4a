package br.edu.unifio.ecommerce.servicos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.unifio.ecommerce.entidades.Aluno;
import br.edu.unifio.ecommerce.entidades.StatusPresenca;
import br.edu.unifio.ecommerce.entidades.StatusViagem;
import br.edu.unifio.ecommerce.entidades.Viagem;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.AlunoRepositorio;
import br.edu.unifio.ecommerce.repositorios.MotoristaRepositorio;
import br.edu.unifio.ecommerce.repositorios.VeiculoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ViagemRepositorio;

@Service
public class ViagemServico {
    private final ViagemRepositorio viagens;
    private final AlunoRepositorio alunos;
    private final MotoristaRepositorio motoristas;
    private final VeiculoRepositorio veiculos;

    public ViagemServico(ViagemRepositorio viagens, AlunoRepositorio alunos,
                         MotoristaRepositorio motoristas, VeiculoRepositorio veiculos) {
        this.viagens = viagens;
        this.alunos = alunos;
        this.motoristas = motoristas;
        this.veiculos = veiculos;
    }

    public List<Viagem> listar() {
        return viagens.findAll();
    }

    public Viagem buscar(Integer id) {
        return viagens.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem " + id + " não encontrada"));
    }

    public List<Viagem> porStatus(StatusViagem status) {
        return viagens.findByStatus(status);
    }

    public List<Viagem> porData(LocalDate data) {
        return viagens.findByData(data);
    }

    public List<Viagem> porMotorista(Integer motoristaId) {
        return viagens.findByMotoristaId(motoristaId);
    }

    @Transactional
    public Viagem salvar(Viagem dados) {
        Viagem viagem = new Viagem();
        copiarDados(viagem, dados);
        viagem.setStatus(StatusViagem.PLANEJADA);
        validarViagem(viagem, null);
        sincronizarPresenca(viagem);
        return viagens.save(viagem);
    }

    @Transactional
    public Viagem atualizar(Integer id, Viagem dados) {
        Viagem viagem = buscar(id);
        copiarDados(viagem, dados);
        if (dados.getStatus() != null) {
            validarTransicao(viagem.getStatus(), dados.getStatus());
            viagem.setStatus(dados.getStatus());
        }
        validarViagem(viagem, id);
        sincronizarPresenca(viagem);
        return viagens.save(viagem);
    }

    @Transactional
    public Viagem atualizarStatus(Integer id, StatusViagem novoStatus) {
        Viagem viagem = buscar(id);
        if (novoStatus == null) {
            throw new ConflitoDeNegocioException("Informe um status válido");
        }
        validarTransicao(viagem.getStatus(), novoStatus);
        viagem.setStatus(novoStatus);
        validarViagem(viagem, id);
        return viagens.save(viagem);
    }

    @Transactional
    public Viagem atualizarPresenca(Integer viagemId, Integer alunoId, StatusPresenca status) {
        Viagem viagem = buscar(viagemId);
        if (status == null) {
            throw new ConflitoDeNegocioException("Informe um status de presença válido");
        }
        if (viagem.getStatus() != StatusViagem.EM_ANDAMENTO) {
            throw new ConflitoDeNegocioException(
                    "A presença só pode ser registrada enquanto a viagem está em andamento");
        }
        if (!viagem.getAlunos().stream().anyMatch(aluno -> aluno.getId().equals(alunoId))) {
            throw new ConflitoDeNegocioException("O aluno não participa desta viagem");
        }
        viagem.getPresenca().put(alunoId, status);
        return viagens.save(viagem);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        viagens.deleteById(id);
    }

    private void copiarDados(Viagem viagem, Viagem dados) {
        viagem.setNome(dados.getNome());
        viagem.setData(dados.getData());
        viagem.setHorarioSaida(dados.getHorarioSaida());
        viagem.setHorarioChegada(dados.getHorarioChegada());
        if (dados.getMotorista() == null || dados.getMotorista().getId() == null) {
            throw new ConflitoDeNegocioException("Informe um motorista válido");
        }
        viagem.setMotorista(motoristas.findById(dados.getMotorista().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista informado não encontrado")));
        if (dados.getVeiculo() == null || dados.getVeiculo().getId() == null) {
            throw new ConflitoDeNegocioException("Informe um veículo válido");
        }
        viagem.setVeiculo(veiculos.findById(dados.getVeiculo().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo informado não encontrado")));

        if (dados.getAlunos() == null || dados.getAlunos().isEmpty()) {
            throw new ConflitoDeNegocioException("A viagem deve possuir pelo menos um aluno");
        }
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        List<Aluno> selecionados = new ArrayList<>();
        for (Aluno referencia : dados.getAlunos()) {
            if (referencia == null || referencia.getId() == null) {
                throw new ConflitoDeNegocioException("Todo aluno deve informar um identificador");
            }
            if (!ids.add(referencia.getId())) {
                throw new ConflitoDeNegocioException("Um aluno não pode ser incluído duas vezes na mesma viagem");
            }
            selecionados.add(alunos.findById(referencia.getId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Aluno " + referencia.getId() + " não encontrado")));
        }
        viagem.setAlunos(selecionados);
    }

    private void validarViagem(Viagem viagem, Integer viagemId) {
        if (viagem.getData() == null || viagem.getHorarioSaida() == null || viagem.getHorarioChegada() == null) {
            throw new ConflitoDeNegocioException("Informe data e horários válidos para a viagem");
        }
        if (viagem.getHorarioChegada().isBefore(viagem.getHorarioSaida())) {
            throw new ConflitoDeNegocioException(
                    "O horário de chegada não pode ser anterior ao horário de saída");
        }
        if (viagem.getAlunos().size() > viagem.getVeiculo().getCapacidade()) {
            throw new ConflitoDeNegocioException("O número de alunos excede a capacidade do veículo");
        }
        if (viagem.getStatus() == StatusViagem.EM_ANDAMENTO) {
            for (Viagem ativa : viagens.buscarEmAndamentoNaData(viagem.getData())) {
                if (viagemId != null && viagemId.equals(ativa.getId())) {
                    continue;
                }
                if (ativa.getMotorista().getId().equals(viagem.getMotorista().getId())
                        || ativa.getVeiculo().getId().equals(viagem.getVeiculo().getId())) {
                    throw new ConflitoDeNegocioException(
                            "Motorista ou veículo já está alocado em outra viagem em andamento nesta data");
                }
            }
        }
    }

    private void sincronizarPresenca(Viagem viagem) {
        var presenca = new LinkedHashMap<Integer, StatusPresenca>();
        for (Aluno aluno : viagem.getAlunos()) {
            presenca.put(aluno.getId(),
                    viagem.getPresenca().getOrDefault(aluno.getId(), StatusPresenca.AGUARDANDO));
        }
        viagem.setPresenca(presenca);
    }

    private void validarTransicao(StatusViagem atual, StatusViagem novo) {
        if (atual == novo) {
            return;
        }
        boolean valida = switch (atual) {
            case PLANEJADA -> novo == StatusViagem.EM_ANDAMENTO;
            case EM_ANDAMENTO -> novo == StatusViagem.CONCLUIDA;
            case CONCLUIDA -> false;
        };
        if (!valida) {
            throw new ConflitoDeNegocioException("Não é possível alterar o status de " + atual + " para " + novo);
        }
    }
}
