package br.com.zup.library.emprestimo;

public class NovoEmprestimoResponse {

    private Long idEmprestimo;
    private String tituloLivro;
    private Long prazoDevolucao;

    public NovoEmprestimoResponse(Emprestimo emprestimo) {
        this.idEmprestimo = emprestimo.getId();
        this.tituloLivro = emprestimo.getExemplar().getLivro().getTitulo();
        this.prazoDevolucao = emprestimo.getPrazoDevolucaoDias();
    }

    public NovoEmprestimoResponse() {
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public Long getPrazoDevolucao() {
        return prazoDevolucao;
    }
}
