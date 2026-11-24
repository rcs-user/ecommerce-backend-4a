package br.edu.unifio.ecommerce.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.unifio.ecommerce.entidades.Responsavel;

public interface ResponsavelRepositorio extends JpaRepository<Responsavel, Integer> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);
}
