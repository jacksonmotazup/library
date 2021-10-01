package br.com.zup.library.exemplar;

import br.com.zup.library.livro.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
    Optional<Exemplar> findFirstByLivroAndDisponivelTrueOrderByTipoCirculacaoDesc(Livro livro);

    Optional<Exemplar> findFirstByLivroAndTipoCirculacaoAndDisponivelTrue(Livro livro, TipoCirculacao tipoCirculacao);


}
