package com.turbo.repository.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.model.exception.NotFoundHttpException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermolaev on 5/18/17.
 */
public abstract class ElasticUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ObjectMapper jsonMapper;

    public static byte[] writeAsJsonBytes(final Object value) {
        try {
            return jsonMapper.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses date from elasticsearch to search entities
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseSearchResponse(SearchResponse response, Class<T> clazz) {
        try {
            List<T> resultList = new ArrayList<>();
            SearchHit[] hints = response.getHits().getHits();

            for(SearchHit hit : hints) {
                resultList.add(
                        jsonMapper.readValue(hit.getSourceAsString(), clazz)
                );
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses date from elasticsearch to ONE search entities(unique)
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseUniqueSearchResponse(SearchResponse response, Class<T> clazz) {
        try {
            SearchHit[] hints = response.getHits().getHits();
            for(SearchHit hit : hints) {
                return jsonMapper.readValue(hit.getSourceAsString(), clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new NotFoundHttpException("Cannot find one " + clazz.getName());
    }

    /**
     * Parses Search ID from elasticsearch to ONE search entities(unique)
     * @param response
     * @return
     */
    public static String parseElasticIdSearchResponse(SearchResponse response) {
        try {
            SearchHit[] hints = response.getHits().getHits();
            for(SearchHit hit : hints) {
                return hit.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new NotFoundHttpException("Cannot find elastic id ");
    }
}
