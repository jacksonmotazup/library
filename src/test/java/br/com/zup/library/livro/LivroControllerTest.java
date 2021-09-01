package br.com.zup.library.livro;

import br.com.zup.library.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static br.com.zup.library.utils.TestFactory.criaNovoLivroRequest;
import static br.com.zup.library.utils.TestFactory.criaNovoLivroRequestEmBranco;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LivroControllerTest {

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LivroRepository livroRepository;


    @Test
    @DisplayName("Deve cadastrar novo livro, retornar status 200 e ID do livro criado")
    void deveCadastrarLivro() throws Exception {
        var request = criaNovoLivroRequest();

        var response = mockMvc.perform(post("/api/v1/livros")
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
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

        var response = mockMvc.perform(post("/api/v1/livros")
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("mensagem").value("ISBN ja cadastrado."))
                .andExpect(MockMvcResultMatchers.jsonPath("campo").value("isbn"))
                .andReturn().getResponse().getContentAsString();

        var livros = livroRepository.findAll();

        assertAll(
                () -> assertEquals(1, livros.size()),
                () -> assertTrue(response.contains("ISBN ja cadastrado.")),
                () -> assertTrue(response.contains("isbn"))
        );
    }

    @Test
    @DisplayName("Não deve cadastrar novo livro com parâmetros em branco")
    void naoDeveCadastrarLivroComParametrosEmBranco() throws Exception {
        var request = criaNovoLivroRequestEmBranco();

        var response = mockMvc.perform(post("/api/v1/livros")
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        var livros = livroRepository.findAll();

        assertAll(
                () -> assertEquals(0, livros.size()),
                () -> assertTrue(response.contains("titulo")),
                () -> assertTrue(response.contains("Titulo deve ser preenchido.")),
                () -> assertTrue(response.contains("preco")),
                () -> assertTrue(response.contains("Preco deve ser preenchido.")),
                () -> assertTrue(response.contains("isbn")),
                () -> assertTrue(response.contains("ISBN deve ser preenchido."))
        );
    }

}