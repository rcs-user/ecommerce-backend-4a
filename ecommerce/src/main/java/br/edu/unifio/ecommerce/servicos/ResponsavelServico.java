package br.edu.unifio.ecommerce.servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.unifio.ecommerce.entidades.Responsavel;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.AlunoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ResponsavelRepositorio;

@Service
public class ResponsavelServico {
    private final ResponsavelRepositorio responsaveis;
    private final AlunoRepositorio alunos;

    public ResponsavelServico(ResponsavelRepositorio responsaveis, AlunoRepositorio alunos) {
        this.responsaveis = responsaveis;
        this.alunos = alunos;
    }

    public List<Responsavel> listar() {
        return responsaveis.findAll();
    }

    public Responsavel buscar(Integer id) {
        return responsaveis.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Responsável " + id + " não encontrado"));
    }

    @Transactional
    public Responsavel salvar(Responsavel responsavel) {
        if (responsaveis.existsByEmailIgnoreCase(responsavel.getEmail())) {
            throw new ConflitoDeNegocioException("Já existe um responsável com este e-mail");
        }
        return responsaveis.save(responsavel);
    }

    @Transactional
    public Responsavel atualizar(Integer id, Responsavel dados) {
        Responsavel atual = buscar(id);
        if (responsaveis.existsByEmailIgnoreCaseAndIdNot(dados.getEmail(), id)) {
            throw new ConflitoDeNegocioException("Já existe um responsável com este e-mail");
        }
        atual.setNome(dados.getNome());
        atual.setTelefone(dados.getTelefone());
        atual.setEmail(dados.getEmail());
        return responsaveis.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (alunos.existsByResponsavelId(id)) {
            throw new ConflitoDeNegocioException("Não é possível excluir um responsável com alunos vinculados");
        }
        responsaveis.deleteById(id);
    }
}
