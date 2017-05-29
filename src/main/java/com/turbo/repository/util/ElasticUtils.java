package com.turbo.repository.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.model.search.SearchConverter;
import com.turbo.model.search.SearchIdentifier;
import org.elasticsearch.action.get.GetResponse;
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

    public static String getElasticTypeWithDate(final String typeName, final LocalDate currentTime) {
        return String.format("%s-%s", typeName, formatter.format(currentTime));
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

    public static <T extends SearchIdentifier> T parseGetResponse(GetResponse response, Class<? extends SearchConverter<T>> clazz) {
        try {
            final SearchConverter<T> jsonBody = jsonMapper.readValue(response.getSourceAsString(), clazz);

            T result = jsonBody.getEntity();
            result.setSearchId(response.getId());

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses date from elasticsearch to search entities and after that, converts them to business objects for service layer
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends SearchIdentifier> List<T> parseSearchResponse(SearchResponse response, Class<? extends SearchConverter<T>> clazz) {
        try {
            List<T> resultList = new ArrayList<>();
            SearchHit[] hints = response.getHits().getHits();

            for(SearchHit hit : hints) {
                final SearchConverter<T> jsonBody = jsonMapper.readValue(hit.getSourceAsString(), clazz);

                T result = jsonBody.getEntity();
                result.setSearchId(hit.getId());

                resultList.add(result);
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
