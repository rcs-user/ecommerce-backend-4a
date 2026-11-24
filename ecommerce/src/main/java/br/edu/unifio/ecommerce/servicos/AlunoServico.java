package br.edu.unifio.ecommerce.servicos;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.unifio.ecommerce.entidades.Aluno;
import br.edu.unifio.ecommerce.exceptions.ConflitoDeNegocioException;
import br.edu.unifio.ecommerce.exceptions.RecursoNaoEncontradoException;
import br.edu.unifio.ecommerce.repositorios.AlunoRepositorio;
import br.edu.unifio.ecommerce.repositorios.ResponsavelRepositorio;
import br.edu.unifio.ecommerce.repositorios.ViagemRepositorio;

@Service
public class AlunoServico {
    private final AlunoRepositorio alunos;
    private final ResponsavelRepositorio responsaveis;
    private final ViagemRepositorio viagens;

    public AlunoServico(AlunoRepositorio alunos, ResponsavelRepositorio responsaveis,
                        ViagemRepositorio viagens) {
        this.alunos = alunos;
        this.responsaveis = responsaveis;
        this.viagens = viagens;
    }

    public List<Aluno> listar() {
        return alunos.findAll();
    }

    public Aluno buscar(Integer id) {
        return alunos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno " + id + " não encontrado"));
    }

    public List<Aluno> porResponsavel(Integer responsavelId) {
        return alunos.findByResponsavelId(responsavelId);
    }

    @Transactional
    public Aluno salvar(Aluno aluno) {
        aluno.setResponsavel(buscarResponsavel(aluno));
        return alunos.save(aluno);
    }

    @Transactional
    public Aluno atualizar(Integer id, Aluno dados) {
        Aluno atual = buscar(id);
        atual.setNome(dados.getNome());
        atual.setEscola(dados.getEscola());
        atual.setTurma(dados.getTurma());
        atual.setResponsavel(buscarResponsavel(dados));
        return alunos.save(atual);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (viagens.existsByAlunosId(id)) {
            throw new ConflitoDeNegocioException("Não é possível excluir um aluno que participa de uma viagem");
        }
        alunos.deleteById(id);
    }

    private br.edu.unifio.ecommerce.entidades.Responsavel buscarResponsavel(Aluno aluno) {
        if (aluno.getResponsavel() == null || aluno.getResponsavel().getId() == null) {
            throw new ConflitoDeNegocioException("Informe um responsável válido");
        }
        return responsaveis.findById(aluno.getResponsavel().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Responsável informado não encontrado"));
    }
}
