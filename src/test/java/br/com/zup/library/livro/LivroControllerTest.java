package br.com.zup.library.livro;

import br.com.zup.library.compartilhado.handler.ExceptionHandlerResponse;
import br.com.zup.library.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static br.com.zup.library.utils.Constantes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class LivroControllerTest {

    public static final String URI_LIVROS = "/api/v1/livros";
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LivroRepository livroRepository;

    @Nested
    class Testes {

        @Test
        @DisplayName("Deve cadastrar novo livro, retornar status 200 e ID do livro criado")
        void deveCadastrarLivro() throws Exception {
            var request = criaNovoLivroRequest();

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_LIVROS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            var livros = livroRepository.findAll();

            assertAll(
                    () -> assertEquals(1, livros.size()),
                    () -> assertEquals(livros.get(0).getId().toString(), response.getContentAsString())
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo livro com isbn existente, retornar status 400")
        void naoDeveCadastrarLivroComIsbnExistente() throws Exception {
            var request = criaNovoLivroRequest();

            livroRepository.save(request.toModel());

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_LIVROS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var livros = livroRepository.count();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(1, livros),
                    () -> assertEquals(1, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("isbn")),
                    () -> assertTrue(resposta.getErros().get("isbn").contains("ISBN já cadastrado"))
            );
        }

        @Test
        @DisplayName("Não deve cadastrar novo livro com parâmetros em branco")
        void naoDeveCadastrarLivroComParametrosEmBranco() throws Exception {
            var request = criaNovoLivroRequestEmBranco();

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_LIVROS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var livros = livroRepository.count();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(0, livros),
                    () -> assertEquals(3, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("titulo")),
                    () -> assertTrue(resposta.getErros().get("titulo").contains("não deve estar em branco")),
                    () -> assertEquals(1, resposta.getErros().get("titulo").size()),
                    () -> assertTrue(resposta.getErros().containsKey("isbn")),
                    () -> assertTrue(resposta.getErros().get("isbn").contains("não deve estar em branco")),
                    () -> assertEquals(1, resposta.getErros().get("isbn").size()),
                    () -> assertTrue(resposta.getErros().containsKey("preco")),
                    () -> assertTrue(resposta.getErros().get("preco").contains("não deve ser nulo")),
                    () -> assertEquals(1, resposta.getErros().get("preco").size()),
                    () -> assertNotNull(resposta.getOcorridoEm())
            );
        }

    }

    //Métodos auxiliares
    private NovoLivroRequest criaNovoLivroRequest() {
        return new NovoLivroRequest(TITULO, PRECO, ISBN);
    }

    private NovoLivroRequest criaNovoLivroRequestEmBranco() {
        return new NovoLivroRequest(STRING_EM_BRANCO, null, STRING_EM_BRANCO);
    }

}