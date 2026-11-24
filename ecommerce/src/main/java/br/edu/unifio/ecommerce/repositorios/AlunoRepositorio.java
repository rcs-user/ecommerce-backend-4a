package br.edu.unifio.ecommerce.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.unifio.ecommerce.entidades.Aluno;

public interface AlunoRepositorio extends JpaRepository<Aluno, Integer> {
    List<Aluno> findByResponsavelId(Integer responsavelId);
    boolean existsByResponsavelId(Integer responsavelId);
}
