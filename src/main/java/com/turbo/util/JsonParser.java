package com.turbo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.model.exception.InternalServerErrorHttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by rakhmetov on 19.04.17.
 */
@Repository
public class JsonParser {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T parse(String string, Class<T> clazz) {
        try {
            return objectMapper.readValue(string, clazz);
        } catch (IOException e) {
            throw new InternalServerErrorHttpException("Json parse problem occurred");
        }
    }

    public String write(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorHttpException("Json write problem occurred");
        }
    }


}
