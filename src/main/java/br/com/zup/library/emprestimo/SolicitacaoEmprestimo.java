package br.com.zup.library.emprestimo;

import br.com.zup.library.livro.Livro;
import br.com.zup.library.usuario.Usuario;

public class SolicitacaoEmprestimo {

    private final Usuario usuario;
    private final Livro livro;
    private final Integer prazoDevolucao;

    public SolicitacaoEmprestimo(Usuario usuario, Livro livro, Integer prazoDevolucao) {
        this.usuario = usuario;
        this.livro = livro;
        this.prazoDevolucao = prazoDevolucao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Integer getPrazoDevolucao() {
        return prazoDevolucao;
    }

    public Livro getLivro() {
        return livro;
    }
}
