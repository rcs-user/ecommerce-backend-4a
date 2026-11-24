package br.edu.unifio.ecommerce.repositorios;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.edu.unifio.ecommerce.entidades.StatusViagem;
import br.edu.unifio.ecommerce.entidades.Viagem;

public interface ViagemRepositorio extends JpaRepository<Viagem, Integer> {
    List<Viagem> findByStatus(StatusViagem status);
    List<Viagem> findByData(LocalDate data);
    List<Viagem> findByMotoristaId(Integer motoristaId);
    List<Viagem> findByVeiculoId(Integer veiculoId);
    boolean existsByAlunosId(Integer alunoId);
    boolean existsByMotoristaId(Integer motoristaId);
    boolean existsByVeiculoId(Integer veiculoId);

    @Query("select v from Viagem v where v.status = br.edu.unifio.ecommerce.entidades.StatusViagem.EM_ANDAMENTO "
            + "and v.data = :data")
    List<Viagem> buscarEmAndamentoNaData(@Param("data") LocalDate data);
}
