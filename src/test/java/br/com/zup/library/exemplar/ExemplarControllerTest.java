package br.com.zup.library.exemplar;

import br.com.zup.library.compartilhado.handler.ExceptionHandlerResponse;
import br.com.zup.library.livro.LivroRepository;
import br.com.zup.library.livro.NovoLivroRequest;
import br.com.zup.library.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static br.com.zup.library.exemplar.TipoCirculacao.LIVRE;
import static br.com.zup.library.exemplar.TipoCirculacao.RESTRITA;
import static br.com.zup.library.utils.Constantes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ExemplarControllerTest {

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExemplarRepository exemplarRepository;
    @Autowired
    private LivroRepository livroRepository;

    @Nested
    class Testes {

        @Test
        @DisplayName("Deve cadastrar um exemplar de um livro com circulação livre, retornar status 200 e id do exemplar")
        void deveCadastrarExemplarCirculacaoLivre() throws Exception {
            var request = criaNovoExemplarRequestLivre();
            var livro = livroRepository.save(criaNovoLivroRequest().toModel());
            var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

            var response = mockMvc.perform(testUtils.aPostWith(request, uri))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            exemplarRepository.findById(Long.valueOf(response))
                    .ifPresentOrElse(exemplar -> assertAll(
                                    () -> assertEquals(LIVRE, exemplar.getCirculacao()),
                                    () -> assertNotNull(exemplar.getLivro())
                            ),
                            () -> fail("Exemplar não encontrado"));
        }

        @Test
        @DisplayName("Deve cadastrar um exemplar de um livro com circulação restrita, retornar status 200 e id do exemplar")
        void deveCadastrarExemplarCirculacaoRestrita() throws Exception {
            var request = criaNovoExemplarRequestRestrita();
            var livro = livroRepository.save(criaNovoLivroRequest().toModel());
            var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

            var response = mockMvc.perform(testUtils.aPostWith(request, uri))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            exemplarRepository.findById(Long.valueOf(response))
                    .ifPresentOrElse(exemplar -> assertAll(
                                    () -> assertEquals(RESTRITA, exemplar.getCirculacao()),
                                    () -> assertNotNull(exemplar.getLivro())
                            ),
                            () -> fail("Exemplar não encontrado"));
        }

        @Test
        @DisplayName("Não deve cadastrar um exemplar de um livro quando ISBN não existir, retornar status 404")
        void naoDeveCadastrarExemplarQuandoIsbnNaoExistir() throws Exception {
            var request = criaNovoExemplarRequestRestrita();
            var uri = "/api/v1/livros/1/exemplares";

            var response = mockMvc.perform(testUtils.aPostWith(request, uri))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getErrorMessage();

            var exemplares = exemplarRepository.count();

            assertAll(
                    () -> assertEquals(0, exemplares),
                    () -> assertEquals("Isbn não encontrado", response)
            );
        }

        @Test
        @DisplayName("Não deve cadastrar um exemplar de um livro quando circulação estiver em branco, retornar status 400")
        void naoDeveCadastrarExemplarComCirculacaoEmBranco() throws Exception {
            var request = criaNovoExemplarRequestEmBranco();
            var livro = livroRepository.save(criaNovoLivroRequest().toModel());
            var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

            var response = mockMvc.perform(testUtils.aPostWith(request, uri))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var exemplares = exemplarRepository.count();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(0, exemplares),
                    () -> assertEquals(1, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("circulacao")),
                    () -> assertTrue(resposta.getErros().get("circulacao").contains("não deve estar em branco"))

            );

        }

        @Test
        @DisplayName("Não deve cadastrar um exemplar de um livro quando circulação não for livre ou restrita, " +
                "retornar status 400")
        void naoDeveCadastrarExemplarComCirculacaoNaoForLivreOuRestrita() throws Exception {
            var request = criaNovoExemplarRequestCirculacaoInvalida();
            var livro = livroRepository.save(criaNovoLivroRequest().toModel());
            var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

            var response = mockMvc.perform(testUtils.aPostWith(request, uri))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var exemplares = exemplarRepository.count();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(0, exemplares),
                    () -> assertEquals(1, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("circulacao")),
                    () -> assertTrue(resposta.getErros().get("circulacao").contains("Circulação deve ser LIVRE ou RESTRITA"))

            );
        }

    }

    //Métodos auxiliares
    private NovoLivroRequest criaNovoLivroRequest() {
        return new NovoLivroRequest(TITULO, PRECO, ISBN);
    }

    private NovoExemplarRequest criaNovoExemplarRequestLivre() {
        var exemplar = new NovoExemplarRequest();
        ReflectionTestUtils.setField(exemplar, CAMPO_CIRCULACAO, CIRCULACAO_LIVRE);
        return exemplar;
    }

    private NovoExemplarRequest criaNovoExemplarRequestRestrita() {
        var exemplar = new NovoExemplarRequest();
        ReflectionTestUtils.setField(exemplar, CAMPO_CIRCULACAO, CIRCULACAO_RESTRITA);
        return exemplar;
    }

    private NovoExemplarRequest criaNovoExemplarRequestEmBranco() {
        return new NovoExemplarRequest();
    }

    private NovoExemplarRequest criaNovoExemplarRequestCirculacaoInvalida() {
        var exemplar = new NovoExemplarRequest();
        ReflectionTestUtils.setField(exemplar, CAMPO_CIRCULACAO, CIRCULACAO_INVALIDA);
        return exemplar;
    }


}