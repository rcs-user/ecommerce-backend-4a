package br.edu.unifio.ecommerce.servicos;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import br.edu.unifio.ecommerce.entidades.Aluno;
import br.edu.unifio.ecommerce.entidades.Motorista;
import br.edu.unifio.ecommerce.entidades.Veiculo;
import br.edu.unifio.ecommerce.entidades.Viagem;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.repositorios.AlunoRepositorio;
import br.edu.unifio.ecommerce.repositorios.MotoristaRepositorio;
import br.edu.unifio.ecommerce.repositorios.VeiculoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ViagemRepositorio;

class ViagemServicoTests {
    @Test
    void impedeQuantidadeDeAlunosMaiorQueCapacidade() {
        ViagemRepositorio viagens = mock(ViagemRepositorio.class);
        AlunoRepositorio alunos = mock(AlunoRepositorio.class);
        MotoristaRepositorio motoristas = mock(MotoristaRepositorio.class);
        VeiculoRepositorio veiculos = mock(VeiculoRepositorio.class);

        Motorista motorista = new Motorista();
        motorista.setId(1);
        Veiculo veiculo = new Veiculo();
        veiculo.setId(1);
        veiculo.setCapacidade(1);
        Aluno aluno1 = new Aluno();
        aluno1.setId(1);
        Aluno aluno2 = new Aluno();
        aluno2.setId(2);
        when(motoristas.findById(1)).thenReturn(Optional.of(motorista));
        when(veiculos.findById(1)).thenReturn(Optional.of(veiculo));
        when(alunos.findById(1)).thenReturn(Optional.of(aluno1));
        when(alunos.findById(2)).thenReturn(Optional.of(aluno2));

        Viagem dados = new Viagem();
        dados.setNome("Linha Centro");
        dados.setData(LocalDate.of(2026, 11, 24));
        dados.setHorarioSaida(LocalTime.of(7, 0));
        dados.setHorarioChegada(LocalTime.of(8, 0));
        dados.setMotorista(motorista);
        dados.setVeiculo(veiculo);
        dados.setAlunos(List.of(aluno1, aluno2));

        ViagemServico servico = new ViagemServico(viagens, alunos, motoristas, veiculos);
        assertThatThrownBy(() -> servico.salvar(dados))
                .isInstanceOf(ConflitoDeNegocioException.class)
                .hasMessageContaining("capacidade");
    }
}
