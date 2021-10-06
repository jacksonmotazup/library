package br.com.zup.library.emprestimo;

import br.com.zup.library.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    long countByUsuario(Usuario usuario);

    List<Emprestimo> findAllByUsuario(Usuario usuario);
}