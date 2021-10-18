package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.usuario.Usuario;

import javax.persistence.*;
import java.time.LocalDate;

import static br.com.zup.library.emprestimo.StatusEmprestimo.DEVOLVIDO;
import static br.com.zup.library.emprestimo.StatusEmprestimo.EMPRESTADO;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long prazoDevolucaoDias;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Exemplar exemplar;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private final LocalDate dataCriacao = LocalDate.now();

    @Column(nullable = false)
    private LocalDate dataEstimadaDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status = EMPRESTADO;

    private LocalDate dataDevolucao;


    public Emprestimo(Long prazoDevolucaoDias, Exemplar exemplar, Usuario usuario) {
        this.prazoDevolucaoDias = prazoDevolucaoDias;
        this.exemplar = exemplar;
        this.usuario = usuario;
        this.dataEstimadaDevolucao = dataCriacao.plusDays(prazoDevolucaoDias);
    }

    /**
     * @deprecated (construtor padrão obrigatório por conta do Hibernate)
     */
    @Deprecated(since = "1.0")
    public Emprestimo() {
    }

    public Long getId() {
        return id;
    }

    public Long getPrazoDevolucaoDias() {
        return prazoDevolucaoDias;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataEstimadaDevolucao() {
        return dataEstimadaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void devolve() {
        this.dataDevolucao = LocalDate.now();
        this.status = DEVOLVIDO;
        this.exemplar.devolve();
    }

    public boolean foiDevolvido() {
        return DEVOLVIDO.equals(this.status);
    }
}
