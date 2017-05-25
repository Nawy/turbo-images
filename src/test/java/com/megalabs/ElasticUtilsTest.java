package com.megalabs;

import com.turbo.repository.util.ElasticUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by ermolaev on 5/23/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LocalDate.class)
public class ElasticUtilsTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate currentDate = LocalDate.from(formatter.parse("2017-01-01"));

    @Test
    public void test_generateManyDataTypes() {
        PowerMockito.mockStatic(LocalDate.class);
        given(LocalDate.now()).willReturn(currentDate);

        final String goldResult = "name-2017-05-23,name-2017-05-22,name-2017-05-21";
        final String result = ElasticUtils.getElasticTypeWithLastDays("name", 3);

        assertThat(goldResult).isEqualTo(result);
    }

    @Test
    public void test() {
        Object val = Math.ceil(30/(double)20);
        System.out.println(val);
    }
}
