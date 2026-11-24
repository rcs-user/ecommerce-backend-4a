package br.edu.unifio.ecommerce.servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.unifio.ecommerce.entidades.Motorista;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.MotoristaRepositorio;
import br.edu.unifio.ecommerce.repositorios.ViagemRepositorio;

@Service
public class MotoristaServico {
    private final MotoristaRepositorio motoristas;
    private final ViagemRepositorio viagens;

    public MotoristaServico(MotoristaRepositorio motoristas, ViagemRepositorio viagens) {
        this.motoristas = motoristas;
        this.viagens = viagens;
    }

    public List<Motorista> listar() {
        return motoristas.findAll();
    }

    public Motorista buscar(Integer id) {
        return motoristas.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista " + id + " não encontrado"));
    }

    @Transactional
    public Motorista salvar(Motorista motorista) {
        if (motoristas.existsByRegistroCnhIgnoreCase(motorista.getRegistroCnh())) {
            throw new ConflitoDeNegocioException("Já existe um motorista com este registro da CNH");
        }
        return motoristas.save(motorista);
    }

    @Transactional
    public Motorista atualizar(Integer id, Motorista dados) {
        Motorista atual = buscar(id);
        if (motoristas.existsByRegistroCnhIgnoreCaseAndIdNot(dados.getRegistroCnh(), id)) {
            throw new ConflitoDeNegocioException("Já existe um motorista com este registro da CNH");
        }
        atual.setNome(dados.getNome());
        atual.setTelefone(dados.getTelefone());
        atual.setRegistroCnh(dados.getRegistroCnh());
        return motoristas.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (viagens.existsByMotoristaId(id)) {
            throw new ConflitoDeNegocioException("Não é possível excluir um motorista vinculado a uma viagem");
        }
        motoristas.deleteById(id);
    }
}
