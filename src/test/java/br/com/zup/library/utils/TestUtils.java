package br.com.zup.library.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static String toJson(Object obj) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
