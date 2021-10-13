package br.com.zup.library.emprestimo;

import br.com.zup.library.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    long countByUsuario(Usuario usuario);

    @Query("SELECT (COUNT (e) > 0) FROM Emprestimo e WHERE e.usuario = ?1 AND e.dataEstimadaEntrega < CURRENT_DATE")
    boolean existeEmprestimoExpirado(Usuario usuario);

}