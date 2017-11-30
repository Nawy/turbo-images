package com.turbo.model.statistic;

import lombok.Data;

@Data
public class UpdateAction {

    private ActionType type;
    private long id;
    private long value;

}
