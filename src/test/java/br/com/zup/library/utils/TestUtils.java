package br.com.zup.library.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    @Autowired
    private ObjectMapper mapper;

    public String toJson(Object obj) throws JsonProcessingException {
        return this.mapper.writeValueAsString(obj);
    }
}
