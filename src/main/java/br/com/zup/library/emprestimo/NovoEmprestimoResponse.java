package br.com.zup.library.emprestimo;

public class NovoEmprestimoResponse {

    private Long idEmprestimo;
    private String tituloLivro;
    private Integer prazoDevolucao;

    public NovoEmprestimoResponse(Emprestimo emprestimo) {
        this.idEmprestimo = emprestimo.getId();
        this.tituloLivro = emprestimo.getExemplar().getLivro().getTitulo();
        this.prazoDevolucao = emprestimo.getPrazoDevolucao();
    }

    public NovoEmprestimoResponse() {
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
