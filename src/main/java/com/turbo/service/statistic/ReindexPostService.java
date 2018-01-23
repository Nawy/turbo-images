package com.turbo.service.statistic;

import com.turbo.model.Post;
import com.turbo.model.UserImage;
import com.turbo.model.search.stat.*;
import com.turbo.repository.elasticsearch.statistic.PostStatisticRepository;
import com.turbo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReindexPostService {

    private final PostStatisticRepository postStatisticRepository;
    private final PostService postService;

    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PostStatEntity getById(final long id) {
        return postStatisticRepository.getById(id);
    }

    public void updateStatisticPost(UpdatePostStatistic reindexPost) {

        log.debug("UPDATE STATIC POST {}", reindexPost);

        PostStatEntity postStatEntity = postStatisticRepository.getById(reindexPost.getId());
        final Post currentPost = postService.getPostById(reindexPost.getId());

        //set empty
        if(Objects.isNull(postStatEntity)) {
            postStatEntity = PostStatEntity
                    .builder()
                    .id(reindexPost.getId())
                    .name(firstNonNull(reindexPost.getName(), currentPost.getName(), ""))
                    .descriptions(
                            Collections.singletonList(
                                    firstNonNull(
                                            reindexPost.getDescription(),
                                            currentPost.getDescription()
                                    )
                            )
                    )
                    .tags(firstNonNull(reindexPost.getTags(), currentPost.getTags()))
                    .days(new ArrayList<>())
                    .weeks(new ArrayList<>())
                    .months(new ArrayList<>())
                    .year(new DiffYear(0L, 0L, 0L))
                    .build();
        } else {
            postStatEntity.setName(reindexPost.getName());
            final List<String> imageDescriptions = currentPost.getImages()
                    .stream()
                    .map(UserImage::getDescription)
                    .collect(Collectors.toList());

            final String postDescription = firstNonNull(reindexPost.getDescription(), "");

            if(!StringUtils.isBlank(postDescription)) {
                imageDescriptions.add(postDescription);
            }

            postStatEntity.setDescriptions(imageDescriptions);
            postStatEntity.setTags(firstNonNull(reindexPost.getTags(), currentPost.getTags()));
        }



        val updatedDays = updateDays(postStatEntity.getDays(), reindexPost);
        val updatedWeeks = updateWeek(postStatEntity.getWeeks(), reindexPost);
        val updatedMonth = updateMonth(postStatEntity.getMonths(), reindexPost);
        val updatedYear = updateYear(postStatEntity.getYear(), reindexPost);

        postStatEntity.setDays(updatedDays);
        postStatEntity.setWeeks(updatedWeeks);
        postStatEntity.setMonths(updatedMonth);
        postStatEntity.setYear(updatedYear);

        // update post in elastic by id, heavy
        postStatisticRepository.updateById(postStatEntity);
    }

    public List<DiffRate> updateDays(
            final List<DiffRate> days,
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
        val day = diffDays.getOrDefault(currentDay, new DiffRate(now,0, 0, 0));

        day.setUps(day.getUps() + post.getUps());
        day.setDowns(day.getDowns() + post.getDowns());
        day.setRating(day.getRating() + post.getRating());

        // upsert
        diffDays.put(currentDay, day);

        return new ArrayList<>(diffDays.values());
    }

    public static List<DiffRate> updateWeek(
            final List<DiffRate> weeks,
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
        val weekStartDelta = LocalDate.now().getDayOfWeek().getValue() - 1;
        val now = LocalDate.now().minusDays(weekStartDelta);
        val currentWeek = weekFormatter.format(now);
        val week = diffWeeks.getOrDefault(
                currentWeek,
                new DiffRate(
                        now,
                        0,
                        0,
                        0
                )
        );
        week.setUps(week.getUps() + post.getUps());
        week.setDowns(week.getDowns() + post.getDowns());
        week.setRating(week.getRating() + post.getRating());

        // upsert
        diffWeeks.put(currentWeek, week);

        return new ArrayList<>(diffWeeks.values());
    }

    public static List<DiffRate> updateMonth(
            final List<DiffRate> weeks,
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
        val monthStartDelta = LocalDate.now().getDayOfMonth() - 1;
        val now = LocalDate.now().minusDays(monthStartDelta);
        val currentMonth = monthFormatter.format(now);
        val month = diffMonthes.getOrDefault(currentMonth, new DiffRate(now, 0, 0, 0));

        month.setUps(month.getUps() + post.getUps());
        month.setDowns(month.getDowns() + post.getDowns());
        month.setRating(month.getRating() + post.getRating());

        // upsert
        diffMonthes.put(currentMonth, month);

        return new ArrayList<>(diffMonthes.values());
    }

    public DiffYear updateYear(
            final DiffYear diffYear,
            UpdatePostStatistic post
    ) {
        return new DiffYear(
                diffYear.getUps() + post.getUps(),
                diffYear.getDowns() + post.getDowns(),
                diffYear.getRating() + post.getRating()
        );
    }
}
