package com.turbo.model.search.stat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by ermolaev on 6/4/17.
 */
@Data
@AllArgsConstructor
public class PostStatEntity {

    private long id;
    private String name;
    private String username;
    private List<String> descriptions;
    private List<String> tags;
    private List<DiffDay> days;
    private List<DiffWeek> weeks;
    private List<DiffMonth> months;
    private DiffYear year;
}
