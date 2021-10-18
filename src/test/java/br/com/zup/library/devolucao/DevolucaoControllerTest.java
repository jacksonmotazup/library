package br.com.zup.library.devolucao;

import br.com.zup.library.emprestimo.Emprestimo;
import br.com.zup.library.emprestimo.EmprestimoRepository;
import br.com.zup.library.exemplar.Exemplar;
import br.com.zup.library.exemplar.ExemplarRepository;
import br.com.zup.library.exemplar.TipoCirculacao;
import br.com.zup.library.livro.Livro;
import br.com.zup.library.livro.LivroRepository;
import br.com.zup.library.usuario.TipoUsuario;
import br.com.zup.library.usuario.Usuario;
import br.com.zup.library.usuario.UsuarioRepository;
import br.com.zup.library.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class DevolucaoControllerTest {

    public final String URI_DEVOLUCOES = "/api/v1/devolucoes";

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private ExemplarRepository exemplarRepository;
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    private Livro livro;
    private Usuario usuario;
    private Exemplar exemplar;
    private Emprestimo emprestimo;


    @BeforeEach
    void setUp() {
        livro = livroRepository.save(new Livro("titulo", BigDecimal.valueOf(100), "123456"));
        usuario = usuarioRepository.save(new Usuario(TipoUsuario.PADRAO));
        exemplar = exemplarRepository.save(new Exemplar(TipoCirculacao.LIVRE, livro));
        emprestimo = emprestimoRepository.save(new Emprestimo(20L, exemplar, usuario));
    }

    @Nested
    class Testes {

        @Test
        @DisplayName("Deve devolver um empréstimo de um exemplar com sucesso, retornar 200")
        void deveDevolverUmExemplarComSucesso() throws Exception {
            var request = new NovaDevolucaoRequest(usuario.getId(), emprestimo.getId());

            mockMvc.perform(testUtils.aPostWith(request, URI_DEVOLUCOES))
                    .andExpect(status().isOk());

            assertAll(
                    () -> assertEquals(LocalDate.now(), emprestimo.getDataDevolucao()),
                    () -> assertTrue(emprestimo.foiDevolvido())
            );
        }

        @Test
        @DisplayName("Não deve devolver um empréstimo de um exemplar já devolvido, retornar 400")
        void naoDeveDevolverUmExemplarJaDevolvido() throws Exception {
            var request = new NovaDevolucaoRequest(usuario.getId(), emprestimo.getId());

            //devolve uma vez
            mockMvc.perform(testUtils.aPostWith(request, URI_DEVOLUCOES))
                    .andExpect(status().isOk());

            //tenta devolver novamente o que já foi devolvido
            var response = mockMvc.perform(testUtils.aPostWith(request, URI_DEVOLUCOES))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            assertAll(
                    () -> assertEquals(LocalDate.now(), emprestimo.getDataDevolucao()),
                    () -> assertTrue(emprestimo.foiDevolvido()),
                    () -> assertEquals("Empréstimo já devolvido", response)
            );
        }

        @Test
        @DisplayName("Não deve devolver um empréstimo de um exemplar que não pertence ao usuário, retornar 404")
        void naoDeveDevolverUmExemplarQueNaoPertenceAoUsuario() throws Exception {
            var usuario2 = usuarioRepository.save(new Usuario(TipoUsuario.PESQUISADOR));
            var request = new NovaDevolucaoRequest(usuario2.getId(), emprestimo.getId());

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_DEVOLUCOES))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            assertAll(
                    () -> assertNull(emprestimo.getDataDevolucao()),
                    () -> assertFalse(emprestimo.foiDevolvido()),
                    () -> assertEquals("Empréstimo não encontrado", response)
            );
        }
    }

}