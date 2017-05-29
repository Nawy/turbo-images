package com.megalabs;

import com.turbo.repository.util.ElasticUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ermolaev on 5/23/17.
 */
public class ElasticUtilsTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate currentDate = LocalDate.now();
    private static final LocalDate currentDate1DayBefore = LocalDate.now().minusDays(1);
    private static final LocalDate currentDate2DayBefore = LocalDate.now().minusDays(2);

    @Test
    public void test_generateManyDataTypes() {

        final String goldResult = String.format(
                "name-%s,name-%s,name-%s",
                formatter.format(currentDate),
                formatter.format(currentDate1DayBefore),
                formatter.format(currentDate2DayBefore)
        );
        final String result = ElasticUtils.getElasticTypeWithLastDays("name", 3);

        assertThat(goldResult).isEqualTo(result);
    }
}
