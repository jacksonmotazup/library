package br.com.zup.library.usuario;

import br.com.zup.library.compartilhado.handler.ExceptionHandlerResponse;
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
import java.time.LocalDate;

import static br.com.zup.library.usuario.TipoUsuario.PADRAO;
import static br.com.zup.library.usuario.TipoUsuario.PESQUISADOR;
import static br.com.zup.library.utils.Constantes.*;
import static org.junit.jupiter.api.Assertions.*;
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

    @Nested
    class Testes {

        @Test
        @DisplayName("deve cadastrar um novo Usuario do tipo PADRAO e retornar 200 e o id do Usuario")
        void deveCadastrarUsuarioTipoPadrao() throws Exception {
            var request = criaNovoUsuarioPadrao();

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_USUARIOS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (NovoUsuarioResponse) testUtils.fromJson(response, NovoUsuarioResponse.class);

            usuarioRepository.findById(resposta.getId())
                    .ifPresentOrElse(usuario -> assertAll(
                                    () -> assertNotNull(usuario),
                                    () -> assertEquals(PADRAO, usuario.getTipoUsuario()),
                                    () -> assertEquals(usuario.getId(), resposta.getId()),
                                    () -> assertEquals(usuario.getTipoUsuario().toString(), resposta.getTipoUsuario()),
                                    () -> assertEquals(LocalDate.now(), resposta.getDataCriacao())
                            ),
                            () -> fail("Usuario não encontrado"));
        }

        @Test
        @DisplayName("deve cadastrar um novo Usuario do tipo PESQUISADOR e retornar 200 e o id do Usuario")
        void deveCadastrarUsuarioTipoPesquisador() throws Exception {
            var request = criaNovoUsuarioPesquisador();

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_USUARIOS))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (NovoUsuarioResponse) testUtils.fromJson(response, NovoUsuarioResponse.class);

            usuarioRepository.findById(resposta.getId())
                    .ifPresentOrElse(usuario -> assertAll(
                                    () -> assertNotNull(usuario),
                                    () -> assertEquals(PESQUISADOR, usuario.getTipoUsuario()),
                                    () -> assertEquals(usuario.getId(), resposta.getId()),
                                    () -> assertEquals(usuario.getTipoUsuario().toString(), resposta.getTipoUsuario()),
                                    () -> assertEquals(LocalDate.now(), resposta.getDataCriacao())
                            ),
                            () -> fail("Usuario não encontrado"));
        }


        @Test
        @DisplayName("Não deve cadastrar um usuario quando tipo estiver em branco, retornar status 400")
        void naoDeveCadastrarUsuarioComTipoEmBranco() throws Exception {
            var request = criaNovoUsuarioEmBranco();

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_USUARIOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var exemplares = usuarioRepository.count();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(0, exemplares),
                    () -> assertEquals(1, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("tipo")),
                    () -> assertTrue(resposta.getErros().get("tipo").contains("não deve estar em branco"))
            );
        }

        @Test
        @DisplayName("Não deve cadastrar um usuario quando tipo não for PADRAO ou PESQUISADOR, retornar status 400")
        void naoDeveCadastrarExemplarComCirculacaoNaoForLivreOuRestrita() throws Exception {
            var request = criaNovoUsuarioTipoInvalido();

            var response = mockMvc.perform(testUtils.aPostWith(request, URI_USUARIOS))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            var exemplares = usuarioRepository.count();

            var resposta = (ExceptionHandlerResponse) testUtils.fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(0, exemplares),
                    () -> assertEquals(1, resposta.getErros().size()),
                    () -> assertTrue(resposta.getErros().containsKey("tipo")),
                    () -> assertTrue(resposta.getErros().get("tipo").contains("Tipo deve ser PADRAO ou PESQUISADOR"))
            );

        }
    }


    //Métodos auxiliares
    private NovoUsuarioRequest criaNovoUsuarioPadrao() {
        var novoUsuarioRequest = new NovoUsuarioRequest();
        ReflectionTestUtils.setField(novoUsuarioRequest, TIPO_USUARIO, USUARIO_PADRAO);
        return novoUsuarioRequest;
    }

    private NovoUsuarioRequest criaNovoUsuarioPesquisador() {
        var novoUsuarioRequest = new NovoUsuarioRequest();
        ReflectionTestUtils.setField(novoUsuarioRequest, TIPO_USUARIO, USUARIO_PESQUISADOR);
        return novoUsuarioRequest;
    }

    private NovoUsuarioRequest criaNovoUsuarioEmBranco() {
        return new NovoUsuarioRequest();
    }

    private NovoUsuarioRequest criaNovoUsuarioTipoInvalido() {
        var novoUsuarioRequest = new NovoUsuarioRequest();
        ReflectionTestUtils.setField(novoUsuarioRequest, TIPO_USUARIO, TIPO_INVALIDO);
        return novoUsuarioRequest;
    }

}