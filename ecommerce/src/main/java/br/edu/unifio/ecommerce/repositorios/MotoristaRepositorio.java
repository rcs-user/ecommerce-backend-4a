package br.edu.unifio.ecommerce.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.unifio.ecommerce.entidades.Motorista;

public interface MotoristaRepositorio extends JpaRepository<Motorista, Integer> {
    boolean existsByRegistroCnhIgnoreCase(String registroCnh);
    boolean existsByRegistroCnhIgnoreCaseAndIdNot(String registroCnh, Integer id);
}
