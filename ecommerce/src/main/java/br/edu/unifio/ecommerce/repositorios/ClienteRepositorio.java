package br.edu.unifio.ecommerce.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.ecommerce.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByDocumento(String documento);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);
    boolean existsByDocumentoAndIdNot(String documento, Integer id);
}
