package br.edu.unifio.ecommerce.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.edu.unifio.ecommerce.entidades.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Integer> {
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Integer id);
    List<Produto> findByCategoriaId(Short categoriaId);

    List<Produto> findByPrecoLessThanEqual(java.math.BigDecimal preco);

    @Query("select p from Produto p where p.estoque < :limite")
    List<Produto> buscarComEstoqueAbaixoDe(@Param("limite") Short limite);
}
