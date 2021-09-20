package br.com.zup.library.emprestimo;

public class NovoEmprestimoResponse {

    private final Long idEmprestimo;
    private final String tituloLivro;
    private final Integer prazoDevolucao;

    public NovoEmprestimoResponse(Emprestimo emprestimo) {
        this.idEmprestimo = emprestimo.getId();
        this.tituloLivro = emprestimo.getExemplar().getLivro().getTitulo();
        this.prazoDevolucao = emprestimo.getPrazoDevolucao();
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public Integer getPrazoDevolucao() {
        return prazoDevolucao;
    }
}
