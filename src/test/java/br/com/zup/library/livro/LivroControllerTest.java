package br.com.zup.library.livro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static br.com.zup.library.utils.TestFactory.criaNovoLivroRequest;
import static br.com.zup.library.utils.TestUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LivroControllerTest {

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
                        .content(toJson(request)))
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

        mockMvc.perform(post("/api/v1/livros")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest());

        var livros = livroRepository.findAll();

        assertAll(
                () -> assertEquals(1, livros.size())
        );
    }

}