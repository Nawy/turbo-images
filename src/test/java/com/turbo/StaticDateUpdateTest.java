package com.turbo;

import com.turbo.model.search.stat.DiffRate;
import com.turbo.model.search.stat.UpdatePostStatistic;
import com.turbo.service.statistic.ReindexPostService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticDateUpdateTest {

    private ReindexPostService postStatisticService;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Before
    public void init() {
        postStatisticService = new ReindexPostService(null, null);
    }

    @Test
    public void shouldCreateDay() {
        final long ratingValue = 1L;
        List<DiffRate> rates = new ArrayList<>();
        UpdatePostStatistic update = UpdatePostStatistic.builder()
                .rating(ratingValue)
                .downs(ratingValue)
                .ups(ratingValue)
                .build();

        rates = postStatisticService.updateDays(rates, update);

        assertThat(rates).isNotNull();
        assertThat(rates).isNotEmpty();
        assertThat(rates.get(0).getRating()).isEqualTo(ratingValue);
        assertThat(rates.get(0).getDowns()).isEqualTo(ratingValue);
        assertThat(rates.get(0).getUps()).isEqualTo(ratingValue);
    }

    @Test
    public void shouldUpdateDay() {
        final long ratingValue = 1L;
        List<DiffRate> rates = new ArrayList<>();

        final LocalDate current = LocalDate.now();
        final DiffRate rate = new DiffRate(current, ratingValue, ratingValue, ratingValue);
        rates.add(rate);

        UpdatePostStatistic update = UpdatePostStatistic.builder()
                .rating(ratingValue)
                .downs(ratingValue)
                .ups(ratingValue)
                .build();

        rates = postStatisticService.updateDays(rates, update);

        assertThat(rates).isNotNull();
        assertThat(rates).isNotEmpty();
        assertThat(rates.get(0).getRating()).isEqualTo(ratingValue+ratingValue);
        assertThat(rates.get(0).getDowns()).isEqualTo(ratingValue+ratingValue);
        assertThat(rates.get(0).getUps()).isEqualTo(ratingValue+ratingValue);
    }

    @Test
    public void shouldCreateWeek() {
    }

    @Test
    public void shouldUpdateWeek() {

    }

    @Test
    public void shouldCreateMonth() {

    }

    @Test
    public void shouldUpdateMonth() {

    }
}
