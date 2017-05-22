package com.turbo.repository.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.model.ElasticIdentifier;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static String getElasticTypeWithCurrentDate(final String typeName) {
        return String.format("%s-%s", typeName, formatter.format(LocalDate.now()));
    }

    public static String getElasticTypeWithLastDays(final String typeName, final int days) {
        StringBuilder result = new StringBuilder(typeName.length()*days*2);
        for(int i = 0; i < days; i++) {
            result.append(
                    String.format("%s-%s",
                            typeName,
                            formatter.format(LocalDate.now().minusDays(i))
                    )
            );
            if(i+1 < days) {
                result.append(",");
            }
        }
        return result.toString();
    }

    public static String getElasticTypeWithoutDate(final String typeName) {
        return String.format("%s-*", typeName);
    }

    public static <T extends ElasticIdentifier> T parseGetResponse(GetResponse response, Class<T> clazz) {
        try {
            final T jsonBody = jsonMapper.readValue(response.getSourceAsString(), clazz);
            jsonBody.setElasticId(response.getId());
            return jsonBody;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends ElasticIdentifier> List<T> parseSearchResponse(SearchResponse response, Class<T> clazz) {
        try {
            List<T> resultList = new ArrayList<>();
            SearchHit[] hints = response.getHits().getHits();

            for(SearchHit hit : hints) {
                final T jsonBody = jsonMapper.readValue(hit.getSourceAsString(), clazz);
                jsonBody.setElasticId(hit.getId());
                resultList.add(jsonBody);
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
