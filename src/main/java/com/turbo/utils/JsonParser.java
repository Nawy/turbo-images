package com.turbo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.model.exception.InternalServerErrorHttpException;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by rakhmetov on 19.04.17.
 */
@Repository
public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parse(String string, Class<T> clazz) {
        try {
            return objectMapper.readValue(string, clazz);
        } catch (IOException e) {
            throw new InternalServerErrorHttpException("Json parse problem occurred");
        }
    }

    public static String write(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorHttpException("Json write problem occurred");
        }
    }


}
