package br.com.zup.library.exemplar;

import br.com.zup.library.livro.LivroRepository;
import br.com.zup.library.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static br.com.zup.library.exemplar.Circulacao.LIVRE;
import static br.com.zup.library.exemplar.Circulacao.RESTRITA;
import static br.com.zup.library.utils.TestFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    @DisplayName("Deve cadastrar um exemplar de um livro com circulação livre, retornar status 200 e id do exemplar")
    void deveCadastrarExemplarCirculacaoLivre() throws Exception {
        var request = criaNovoExemplarRequestLivre();
        var livro = livroRepository.save(criaNovoLivroRequest().toModel());
        var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

        var response = mockMvc.perform(post(uri)
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var exemplares = exemplarRepository.findAll();
        var exemplar = exemplares.get(0);

        assertAll(
                () -> assertEquals(1, exemplares.size()),
                () -> assertEquals(exemplar.getId().toString(), response),
                () -> assertEquals(LIVRE, exemplar.getCirculacao()),
                () -> assertNotNull(exemplar.getLivro())
        );
    }

    @Test
    @DisplayName("Deve cadastrar um exemplar de um livro com circulação restrita, retornar status 200 e id do exemplar")
    void deveCadastrarExemplarCirculacaoRestrita() throws Exception {
        var request = criaNovoExemplarRequestRestrita();
        var livro = livroRepository.save(criaNovoLivroRequest().toModel());
        var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

        var response = mockMvc.perform(post(uri)
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var exemplares = exemplarRepository.findAll();
        var exemplar = exemplares.get(0);

        assertAll(
                () -> assertEquals(1, exemplares.size()),
                () -> assertEquals(exemplar.getId().toString(), response),
                () -> assertEquals(RESTRITA, exemplar.getCirculacao()),
                () -> assertNotNull(exemplar.getLivro())
        );
    }

    @Test
    @DisplayName("Não deve cadastrar um exemplar de um livro quando ISBN não existir, retornar status 404")
    void naoDeveCadastrarExemplarQuandoIsbnNaoExistir() throws Exception {
        var request = criaNovoExemplarRequestRestrita();

        var response = mockMvc.perform(post("/api/v1/livros/1/exemplares")
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getErrorMessage();

        var exemplares = exemplarRepository.findAll();

        assertAll(
                () -> assertEquals(0, exemplares.size()),
                () -> assertEquals("Isbn não encontrado", response)
        );
    }

    @Test
    @DisplayName("Não deve cadastrar um exemplar de um livro quando circulação estiver em branco, retornar status 400")
    void naoDeveCadastrarExemplarComCirculacaoEmBranco() throws Exception {
        var request = criaNovoExemplarRequestEmBranco();
        var livro = livroRepository.save(criaNovoLivroRequest().toModel());
        var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

        mockMvc.perform(post(uri)
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem").value("não deve estar em branco"))
                .andExpect(jsonPath("campo").value("circulacao"))
                .andReturn().getResponse().getContentAsString();

        var exemplares = exemplarRepository.findAll();

        assertEquals(0, exemplares.size());
    }

    @Test
    @DisplayName("Não deve cadastrar um exemplar de um livro quando circulação não for livre ou restrita, " +
            "retornar status 400")
    void naoDeveCadastrarExemplarComCirculacaoNaoForLivreOuRestrita() throws Exception {
        var request = criaNovoExemplarRequestCirculacaoInvalida();
        var livro = livroRepository.save(criaNovoLivroRequest().toModel());
        var uri = String.format("/api/v1/livros/%s/exemplares", livro.getIsbn());

        mockMvc.perform(post(uri)
                        .contentType(APPLICATION_JSON)
                        .content(testUtils.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem").value("Circulação deve ser livre ou restrita"))
                .andExpect(jsonPath("campo").value("circulacao"))
                .andReturn().getResponse().getContentAsString();

        var exemplares = exemplarRepository.findAll();

        assertEquals(0, exemplares.size());
    }


}