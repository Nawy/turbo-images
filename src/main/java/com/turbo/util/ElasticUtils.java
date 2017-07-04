package com.turbo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermolaev on 5/18/17.
 */
@Repository
public class ElasticUtils {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper jsonMapper;

    @Autowired
    public ElasticUtils(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public byte[] writeAsJsonBytes(final Object value) {
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
    public <T> List<T> parseSearchResponse(SearchResponse response, Class<T> clazz) {
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
    public <T> T parseUniqueSearchResponse(SearchResponse response, Class<T> clazz) {
        try {
            SearchHit[] hints = response.getHits().getHits();
            for(SearchHit hit : hints) {
                return jsonMapper.readValue(hit.getSourceAsString(), clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Parses Search ID from elasticsearch to ONE search entities(unique)
     * @param response
     * @return
     */
    public String parseElasticIdSearchResponse(SearchResponse response) {
        try {
            SearchHit[] hints = response.getHits().getHits();
            for(SearchHit hit : hints) {
                return hit.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static String getTypePerYear(final String typeName, final LocalDate currentDate) {
        return String.format("%s%d", typeName,currentDate.getYear());
    }
}
