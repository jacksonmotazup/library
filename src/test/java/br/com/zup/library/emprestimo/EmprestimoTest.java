package br.com.zup.library.emprestimo;

import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.exemplar.TipoCirculacao;
import br.com.zup.library.livro.Livro;
import br.com.zup.library.usuario.TipoUsuario;
import br.com.zup.library.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class EmprestimoTest {

    @Nested
    class EmprestimoExpirado {

        @Test
        @DisplayName("Deve retornar TRUE quando o empréstimo está expirado por 1 dia")
        void deveRetornarTrueComEmprestimoExpiradoPorUmDia() {
            var emprestimo = criaEmprestimo();

            setField(emprestimo, "dataCriacao", LocalDate.now().minusDays(11));

            assertTrue(emprestimo.emprestimoExpirado());
        }

        @Test
        @DisplayName("Deve retornar TRUE quando o empréstimo está expirado por vários dias")
        void deveRetornarTrueComEmprestimoExpiradoPorVariosDias() {
            var emprestimo = criaEmprestimo();

            setField(emprestimo, "dataCriacao", LocalDate.now().minusDays(50));

            assertTrue(emprestimo.emprestimoExpirado());
        }

        @Test
        @DisplayName("Deve retornar FALSE quando o empréstimo não está expirado e feito no mesmo dia")
        void deveRetornarFalseSemEmprestimoExpirado() {
            var emprestimo = criaEmprestimo();

            assertFalse(emprestimo.emprestimoExpirado());
        }

        @Test
        @DisplayName("Deve retornar FALSE quando o empréstimo não está expirado por vencer no mesmo dia")
        void deveRetornarFalseSemEmprestimoExpiradoVencendoNoMesmoDia() {
            var emprestimo = criaEmprestimo();

            setField(emprestimo, "dataCriacao", LocalDate.now().minusDays(10));

            assertFalse(emprestimo.emprestimoExpirado());
        }

    }


    private Emprestimo criaEmprestimo() {
        return new Emprestimo(10L,
                new Exemplar(TipoCirculacao.LIVRE,
                        new Livro("titulo", BigDecimal.valueOf(100), "123456")),
                new Usuario(TipoUsuario.PADRAO));
    }

}