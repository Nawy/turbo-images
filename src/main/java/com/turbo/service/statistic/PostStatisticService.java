package com.turbo.service.statistic;

import com.turbo.model.search.stat.PostStatEntity;
import com.turbo.model.statistic.ReindexPost;
import com.turbo.repository.elasticsearch.statistic.PostStatisticRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class PostStatisticService {

    private PostStatisticRepository postStatisticRepository;

    public PostStatEntity getById(final long id) {
        return postStatisticRepository.getById(id);
    }

    public void updateStaticPost(
            ReindexPost reindexPost,
            final LocalDate date
    ) {
        final PostStatEntity postStatEntity = postStatisticRepository.getById(reindexPost.getId());
    }
}
