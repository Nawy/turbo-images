package com.turbo.repository.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by ermolaev on 5/18/17.
 */
public abstract class ElasticUtils {

    public static ObjectMapper jsonMapper;

    public static byte[] writeAsJsonBytes(final Object value) {
        try {
            return jsonMapper.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
