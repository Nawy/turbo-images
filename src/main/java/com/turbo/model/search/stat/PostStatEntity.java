package com.turbo.model.search.stat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by ermolaev on 6/4/17.
 */
@Data
@AllArgsConstructor
@Builder
public class PostStatEntity {

    private long id;
    private String name;
    private String username;
    private List<String> descriptions;
    private Set<String> tags;
    private List<DiffRate> days;
    private List<DiffRate> weeks;
    private List<DiffRate> months;
    private DiffYear year;
}
