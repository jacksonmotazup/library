package br.com.zup.library.emprestimo;

import br.com.zup.library.compartilhado.handler.ExceptionHandlerResponse;
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
import java.util.List;

import static br.com.zup.library.exemplar.TipoCirculacao.LIVRE;
import static br.com.zup.library.exemplar.TipoCirculacao.RESTRITA;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class EmprestimoControllerTest {

    public final String URI_EMPRESTIMOS = "/api/v1/emprestimos";

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
    private Usuario usuarioPadrao;
    private Usuario usuarioPesquisador;


    @BeforeEach
    void setUp() {
        livro = livroRepository.save(new Livro("titulo", BigDecimal.valueOf(100), "123456"));
        usuarioPadrao = usuarioRepository.save(new Usuario(TipoUsuario.PADRAO));
        usuarioPesquisador = usuarioRepository.save(new Usuario(TipoUsuario.PESQUISADOR));
    }

    @Nested
    class Testes {

        @Test
        @DisplayName("Deve cadastrar novo emprestimo de um exemplar livre de um livro para um usuário padrão, retornar 200")
        void deveCadastrarNovoEmprestimoExemplarLivreUsuarioPadrao() throws Exception {
            var exemplarLivre = salvaExemplar(LIVRE);
            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (NovoEmprestimoResponse) testUtils.fromJson(response, NovoEmprestimoResponse.class);

            emprestimoRepository.findById(resposta.getIdEmprestimo()).ifPresentOrElse(emprestimo -> assertAll(
                            () -> assertEquals(resposta.getIdEmprestimo(), emprestimo.getId()),
                            () -> assertEquals(resposta.getTituloLivro(), emprestimo.getExemplar().getLivro().getTitulo()),
                            () -> assertEquals(resposta.getPrazoDevolucao(), emprestimo.getPrazoDevolucaoDias()),
                            () -> assertEquals(LocalDate.now(), emprestimo.getDataCriacao()),
                            () -> assertEquals(usuarioPadrao, emprestimo.getUsuario()),
                            () -> assertFalse(exemplarLivre.isDisponivel())
                    ),
                    () -> fail("Exemplar não encontrado")
            );
        }

        @Test
        @DisplayName("Deve cadastrar novo emprestimo de um exemplar livre de um livro para um usuário pesquisador, retornar 200")
        void deveCadastrarNovoEmprestimoExemplarLivreUsuarioPesquisador() throws Exception {
            var exemplarLivre = salvaExemplar(LIVRE);
            var request = montaRequest(usuarioPesquisador);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (NovoEmprestimoResponse) testUtils.fromJson(response, NovoEmprestimoResponse.class);

            emprestimoRepository.findById(resposta.getIdEmprestimo()).ifPresentOrElse(emprestimo -> assertAll(
                            () -> assertEquals(resposta.getIdEmprestimo(), emprestimo.getId()),
                            () -> assertEquals(resposta.getTituloLivro(), emprestimo.getExemplar().getLivro().getTitulo()),
                            () -> assertEquals(resposta.getPrazoDevolucao(), emprestimo.getPrazoDevolucaoDias()),
                            () -> assertEquals(LocalDate.now(), emprestimo.getDataCriacao()),
                            () -> assertEquals(usuarioPesquisador, emprestimo.getUsuario()),
                            () -> assertFalse(exemplarLivre.isDisponivel())
                    ),
                    () -> fail("Exemplar não encontrado")
            );
        }

        @Test
        @DisplayName("Deve cadastrar novo emprestimo de um exemplar restrito de um livro para um usuário pesquisador, retornar 200")
        void deveCadastrarNovoEmprestimoExemplarRestritoUsuarioPesquisador() throws Exception {
            var exemplarRestrito = salvaExemplar(RESTRITA);
            var request = montaRequest(usuarioPesquisador);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (NovoEmprestimoResponse) testUtils.fromJson(response, NovoEmprestimoResponse.class);

            emprestimoRepository.findById(resposta.getIdEmprestimo()).ifPresentOrElse(emprestimo -> assertAll(
                            () -> assertEquals(resposta.getIdEmprestimo(), emprestimo.getId()),
                            () -> assertEquals(resposta.getTituloLivro(), emprestimo.getExemplar().getLivro().getTitulo()),
                            () -> assertEquals(resposta.getPrazoDevolucao(), emprestimo.getPrazoDevolucaoDias()),
                            () -> assertEquals(LocalDate.now(), emprestimo.getDataCriacao()),
                            () -> assertEquals(usuarioPesquisador, emprestimo.getUsuario()),
                            () -> assertFalse(exemplarRestrito.isDisponivel())
                    ),
                    () -> fail("Exemplar não encontrado")
            );
        }

        @Test
        @DisplayName("Deve cadastrar novo emprestimo de um exemplar de um livro para um usuário pesquisador " +
                "sem preencher prazo, retornar 200")
        void deveCadastrarNovoEmprestimoExemplarUsuarioPesquisadorSemPreencherPrazo() throws Exception {
            var exemplarRestrito = salvaExemplar(RESTRITA);
            var request = montaRequestPrazoNulo(usuarioPesquisador);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (NovoEmprestimoResponse) testUtils.fromJson(response, NovoEmprestimoResponse.class);

            emprestimoRepository.findById(resposta.getIdEmprestimo()).ifPresentOrElse(emprestimo -> assertAll(
                            () -> assertEquals(resposta.getIdEmprestimo(), emprestimo.getId()),
                            () -> assertEquals(resposta.getTituloLivro(), emprestimo.getExemplar().getLivro().getTitulo()),
                            () -> assertEquals(resposta.getPrazoDevolucao(), emprestimo.getPrazoDevolucaoDias()),
                            () -> assertEquals(60, emprestimo.getPrazoDevolucaoDias()),
                            () -> assertEquals(LocalDate.now(), emprestimo.getDataCriacao()),
                            () -> assertEquals(usuarioPesquisador, emprestimo.getUsuario()),
                            () -> assertFalse(exemplarRestrito.isDisponivel())
                    ),
                    () -> fail("Exemplar não encontrado")
            );
        }


        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar restrito de um livro para um usuário padrão, " +
                "retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarRestritoUsuarioPadrao() throws Exception {
            salvaExemplar(RESTRITA);
            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Não existe exemplar disponível", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar de um livro para um usuário padrão " +
                "sem preencher prazo, retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarUsuarioPadraoSemDigitarPrazo() throws Exception {
            salvaExemplar(LIVRE);
            var request = montaRequestPrazoNulo(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Prazo deve ser preenchido", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar de um livro para um usuário padrão " +
                "com mais de 5 empréstimos cadastrados, retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarUsuarioPadraoComMaisDe5EmprestimosCadastrados() throws Exception {
            var exemplar = new Exemplar(LIVRE, livro);
            exemplarRepository.saveAll(List.of(exemplar, exemplar, exemplar, exemplar, exemplar, exemplar, exemplar));

            var emprestimo1 = new Emprestimo(10L, exemplar, usuarioPadrao);
            var emprestimo2 = new Emprestimo(10L, exemplar, usuarioPadrao);
            var emprestimo3 = new Emprestimo(10L, exemplar, usuarioPadrao);
            var emprestimo4 = new Emprestimo(10L, exemplar, usuarioPadrao);
            var emprestimo5 = new Emprestimo(10L, exemplar, usuarioPadrao);
            emprestimoRepository.saveAll(List.of(emprestimo1, emprestimo2, emprestimo3, emprestimo4, emprestimo5));

            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Usuário padrão só pode ter 5 empréstimos simultâneos", response),
                    () -> assertEquals(5, emprestimos)
            );
        }

        @Test
        @DisplayName("Deve cadastrar novo emprestimo de um exemplar de um livro para um usuário pesquisador " +
                "com mais de 5 empréstimos cadastrados, retornar 200")
        void deveCadastrarNovoEmprestimoExemplarUsuarioPesquisadorComMaisDe5EmprestimosCadastrados() throws Exception {
            var exemplar = new Exemplar(LIVRE, livro);
            exemplarRepository.saveAll(List.of(exemplar, exemplar, exemplar, exemplar, exemplar, exemplar, exemplar));

            var emprestimo1 = new Emprestimo(10L, exemplar, usuarioPesquisador);
            var emprestimo2 = new Emprestimo(10L, exemplar, usuarioPesquisador);
            var emprestimo3 = new Emprestimo(10L, exemplar, usuarioPesquisador);
            var emprestimo4 = new Emprestimo(10L, exemplar, usuarioPesquisador);
            var emprestimo5 = new Emprestimo(10L, exemplar, usuarioPesquisador);
            emprestimoRepository.saveAll(List.of(emprestimo1, emprestimo2, emprestimo3, emprestimo4, emprestimo5));

            var request = montaRequest(usuarioPesquisador);

            mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("tituloLivro").value(livro.getTitulo()))
                    .andExpect(jsonPath("prazoDevolucao").value(request.getPrazoDevolucaoDias()));

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals(6, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar de um livro para um usuário padrão " +
                "sem exemplar disponivel, retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarUsuarioPadraoSemExemplarDisponivel() throws Exception {
            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Não existe exemplar disponível", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar de um livro para um usuário pesquisador " +
                "sem exemplar disponivel, retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarUsuarioPesquisadorSemExemplarDisponivel() throws Exception {
            var request = montaRequest(usuarioPesquisador);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Não existe exemplar disponível", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar de um livro para um usuário pesquisador " +
                "com exemplar indisponivel, retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarUsuarioPesquisadorComExemplarIndisponivel() throws Exception {
            var exemplar = new Exemplar(LIVRE, livro);
            exemplar.reserva();
            exemplarRepository.save(exemplar);
            var request = montaRequest(usuarioPesquisador);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Não existe exemplar disponível", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo emprestimo de um exemplar de um livro para um usuário padrao " +
                "com exemplar indisponivel, retornar 400")
        void naoDeveCadastrarNovoEmprestimoExemplarUsuarioPadraoComExemplarIndisponivel() throws Exception {
            var exemplar = new Exemplar(LIVRE, livro);
            exemplar.reserva();
            exemplarRepository.save(exemplar);
            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Não existe exemplar disponível", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar emprestimo com request inválido")
        void naoDeveCadastrarNovoEmprestimoComRequestInvalido() throws Exception {
            salvaExemplar(LIVRE);
            var request = new NovoEmprestimoRequest(null, null, 61L);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals(3, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("idLivro")),
                    () -> assertTrue(resposta.getErros().containsKey("idUsuario")),
                    () -> assertTrue(resposta.getErros().containsKey("prazoDevolucaoDias")),
                    () -> assertTrue(resposta.getErros().get("prazoDevolucaoDias").contains("deve estar entre 1 e 60")),
                    () -> assertTrue(resposta.getErros().get("idLivro").contains("não deve ser nulo")),
                    () -> assertTrue(resposta.getErros().get("idUsuario").contains("não deve ser nulo")),
                    () -> assertNotNull(resposta.getOcorridoEm()),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar emprestimo quando usuario não existir")
        void naoDeveCadastrarEmprestimoQuandoUsuarioNaoExistir() throws Exception {
            usuarioRepository.deleteAll();

            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Usuário não encontrado", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar emprestimo quando livro não existir")
        void naoDeveCadastrarEmprestimoQuandoLivroNaoExistir() throws Exception {
            livroRepository.deleteAll();

            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            var emprestimos = emprestimoRepository.count();

            assertAll(
                    () -> assertEquals("Livro não encontrado", response),
                    () -> assertEquals(0, emprestimos)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar empréstimo com usuário tendo empréstimo expirado, retornar 400")
        void naoDeveCadastrarEmprestimoComEmprestimoExpirado() throws Exception {
            var exemplar = salvaExemplar(LIVRE);
            var emprestimo = new Emprestimo(10L, exemplar, usuarioPadrao);
            setField(emprestimo, "dataCriacao", LocalDate.now().minusDays(11L));

            emprestimoRepository.save(emprestimo);

            var request = montaRequest(usuarioPadrao);

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_EMPRESTIMOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            assertEquals("Existe empréstimo expirado", response);
        }
    }


    //Métodos úteis
    private Exemplar salvaExemplar(TipoCirculacao tipoCirculacao) {
        return exemplarRepository.save(new Exemplar(tipoCirculacao, livro));
    }

    private NovoEmprestimoRequest montaRequestPrazoNulo(Usuario usuario) {
        return new NovoEmprestimoRequest(livro.getId(), usuario.getId(), null);
    }

    private NovoEmprestimoRequest montaRequest(Usuario usuario) {
        return new NovoEmprestimoRequest(livro.getId(), usuario.getId(), 20L);
    }

}