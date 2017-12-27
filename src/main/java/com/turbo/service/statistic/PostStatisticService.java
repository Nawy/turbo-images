package com.turbo.service.statistic;

import com.turbo.model.search.stat.*;
import com.turbo.model.statistic.ReindexPost;
import com.turbo.repository.elasticsearch.statistic.PostStatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostStatisticService {

    private PostStatisticRepository postStatisticRepository;
    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM-dd");
    private static DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("ww");
    private static DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

    public PostStatEntity getById(final long id) {
        return postStatisticRepository.getById(id);
    }

    public void updateStaticPost(UpdatePostStatistic reindexPost) {

        final PostStatEntity postStatEntity = postStatisticRepository.getById(reindexPost.getId());
        val updatedDays = updateDays(postStatEntity.getDays(), reindexPost);
        val updatedWeeks = updateWeek(postStatEntity.getWeeks(), reindexPost);
        val updatedMonth = updateMonth(postStatEntity.getMonths(), reindexPost);

        postStatEntity.setDays(updatedDays);
        postStatEntity.setWeeks(updatedWeeks);
        postStatEntity.setMonths(updatedMonth);

        postStatEntity.getYear().setDowns(reindexPost.getDowns());
        postStatEntity.getYear().setUps(reindexPost.getUps());
        postStatEntity.getYear().setRating(reindexPost.getRating());

        postStatEntity.setName(reindexPost.getName());
        postStatEntity.setTags(reindexPost.getTags());

        // update post in elastic by id, heavy
        postStatisticRepository.updateById(postStatEntity);
    }

    private List<DiffDay> updateDays(
            final List<DiffDay> days,
            UpdatePostStatistic post
    ) {
        val diffDays = days.stream()
                .collect(
                        Collectors.toMap(
                                value -> dayFormatter.format(value.getDate()),
                                Function.identity(),
                                (v1, v2) -> v1
                        )
                );
        val now = LocalDate.now();
        val currentDay = dayFormatter.format(now);
        val day = diffDays.getOrDefault(currentDay, new DiffDay(now, 0, 0, 0));

        day.setUps(day.getUps() + post.getUps());
        day.setDowns(day.getDowns() + post.getDowns());
        day.setRating(day.getRating() + post.getRating());

        // upsert
        diffDays.put(currentDay, day);

        return new ArrayList<>(diffDays.values());
    }

    private List<DiffWeek> updateWeek(
            final List<DiffWeek> weeks,
            UpdatePostStatistic post
    ) {
        val diffWeeks = weeks.stream()
                .collect(
                        Collectors.toMap(
                                value -> weekFormatter.format(value.getDate()),
                                Function.identity(),
                                (v1, v2) -> v1
                        )
                );
        val now = LocalDate.now();
        val currentWeek = weekFormatter.format(now);
        val week = diffWeeks.getOrDefault(currentWeek, new DiffWeek(now, 0, 0, 0));

        week.setUps(week.getUps() + post.getUps());
        week.setDowns(week.getDowns() + post.getDowns());
        week.setRating(week.getRating() + post.getRating());

        // upsert
        diffWeeks.put(currentWeek, week);

        return new ArrayList<>(diffWeeks.values());
    }

    private List<DiffMonth> updateMonth(
            final List<DiffMonth> weeks,
            UpdatePostStatistic post
    ) {
        val diffMonthes = weeks.stream()
                .collect(
                        Collectors.toMap(
                                value -> weekFormatter.format(value.getDate()),
                                Function.identity(),
                                (v1, v2) -> v1
                        )
                );
        val now = LocalDate.now();
        val currentMonth = monthFormatter.format(now);
        val month = diffMonthes.getOrDefault(currentMonth, new DiffMonth(now, 0, 0, 0));

        month.setUps(month.getUps() + post.getUps());
        month.setDowns(month.getDowns() + post.getDowns());
        month.setRating(month.getRating() + post.getRating());

        // upsert
        diffMonthes.put(currentMonth, month);

        return new ArrayList<>(diffMonthes.values());
    }
}
