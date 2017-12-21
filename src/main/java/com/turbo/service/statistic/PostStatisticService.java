package com.turbo.service.statistic;

import com.turbo.repository.elasticsearch.statistic.PostStatisticRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostStatisticService {

    private PostStatisticRepository postStatisticRepository;
}
