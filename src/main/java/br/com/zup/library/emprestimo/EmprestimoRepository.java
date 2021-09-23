package br.com.zup.library.emprestimo;

import br.com.zup.library.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @Query("SELECT (COUNT(e) >= 5) FROM Emprestimo e WHERE e.usuario = ?1")
    boolean existeLimiteEmprestimoPorUsuario(Usuario usuario);

}