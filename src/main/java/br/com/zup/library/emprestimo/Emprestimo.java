package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.usuario.Usuario;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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

    public void devolve() {
        this.dataDevolucao = LocalDate.now();
        this.exemplar.devolve();
    }

    public boolean foiDevolvido() {
        return !Objects.isNull(this.dataDevolucao);
    }
}
