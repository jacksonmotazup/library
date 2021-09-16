package br.com.zup.library.utils;

import br.com.zup.library.usuario.NovoUsuarioResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    @Autowired
    private ObjectMapper mapper;

    private String toJson(Object obj) throws JsonProcessingException {
        return this.mapper.writeValueAsString(obj);
    }

    public MockHttpServletRequestBuilder aPostWith(Object request,
                                                   String uri) throws JsonProcessingException {
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(toJson(request));
    }

    public NovoUsuarioResponse fromJson(String response, Class<?> novoUsuarioResponseClass) throws JsonProcessingException {
        return (NovoUsuarioResponse) mapper.readValue(response, novoUsuarioResponseClass);
    }
}
