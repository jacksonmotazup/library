package br.com.zup.library.usuario;

import br.com.zup.library.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static br.com.zup.library.usuario.TipoUsuario.PADRAO;
import static br.com.zup.library.usuario.TipoUsuario.PESQUISADOR;
import static br.com.zup.library.utils.TestFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UsuarioControllerTest {

    public final String URI_USUARIOS = "/api/v1/usuarios";
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("deve cadastrar um novo Usuario do tipo PADRAO e retornar 200 e o id do Usuario")
    void deveCadastrarUsuarioTipoPadrao() throws Exception {
        var request = criaNovoUsuarioPadrao();

        var response = mockMvc.perform(testUtils.montaRequisicaoPost(request, URI_USUARIOS))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var resposta = testUtils.fromJson(response, NovoUsuarioResponse.class);

        usuarioRepository.findById(resposta.getId())
                .ifPresent(usuario -> assertAll(
                                () -> assertNotNull(usuario),
                                () -> assertEquals(PADRAO, usuario.getTipoUsuario()),
                                () -> assertEquals(usuario.getId(), resposta.getId()),
                                () -> assertEquals(usuario.getTipoUsuario().toString(), resposta.getTipoUsuario()),
                                () -> assertEquals(LocalDate.now(), resposta.getDataCriacao())
                        )
                );


    }

    @Test
    @DisplayName("deve cadastrar um novo Usuario do tipo PESQUISADOR e retornar 200 e o id do Usuario")
    void deveCadastrarUsuarioTipoPesquisador() throws Exception {
        var request = criaNovoUsuarioPesquisador();

        var response = mockMvc.perform(testUtils.montaRequisicaoPost(request, URI_USUARIOS))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var resposta = testUtils.fromJson(response, NovoUsuarioResponse.class);

        var usuario = usuarioRepository.findById(resposta.getId())
                .orElse(fail());

        assertAll(
                () -> assertNotNull(usuario),
                () -> assertEquals(PESQUISADOR, usuario.getTipoUsuario()),
                () -> assertEquals(usuario.getId(), resposta.getId()),
                () -> assertEquals(usuario.getTipoUsuario().toString(), resposta.getTipoUsuario()),
                () -> assertEquals(LocalDate.now(), resposta.getDataCriacao())
        );
    }


    @Test
    @DisplayName("Não deve cadastrar um usuario quando tipo estiver em branco, retornar status 400")
    void naoDeveCadastrarUsuarioComTipoEmBranco() throws Exception {
        var request = criaNovoUsuarioEmBranco();

        mockMvc.perform(testUtils.montaRequisicaoPost(request, URI_USUARIOS))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem").value("não deve estar em branco"))
                .andExpect(jsonPath("campo").value("tipo"));

        var exemplares = usuarioRepository.findAll();

        assertEquals(0, exemplares.size());
    }

    @Test
    @DisplayName("Não deve cadastrar um usuario quando tipo não for PADRAO ou PESQUISADOR, retornar status 400")
    void naoDeveCadastrarExemplarComCirculacaoNaoForLivreOuRestrita() throws Exception {
        var request = criaNovoUsuarioTipoInvalido();

        mockMvc.perform(testUtils.montaRequisicaoPost(request, URI_USUARIOS))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem").value("Tipo deve ser PADRAO ou PESQUISADOR"))
                .andExpect(jsonPath("campo").value("tipo"));

        var exemplares = usuarioRepository.findAll();

        assertEquals(0, exemplares.size());
    }

}