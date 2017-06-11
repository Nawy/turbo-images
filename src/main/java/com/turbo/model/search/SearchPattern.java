package com.turbo.model.search;

import com.turbo.model.Nullable;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * Created by rakhmetov on 11.06.17.
 */
public class SearchPattern {
    private final SearchPeriod period;
    private final SearchSort sort;
    private final SearchOrder order;

    public SearchPattern(@Nullable SearchPeriod period, @Nullable SearchSort sort, @Nullable SearchOrder order) {
        this.period = firstNonNull(period, SearchPeriod.ALL_TIME);
        this.sort = firstNonNull(sort, SearchSort.RATING);
        this.order = firstNonNull(order, SearchOrder.DESC);
    }

    public SearchPeriod getPeriod() {
        return period;
    }

    public SearchSort getSort() {
        return sort;
    }

    public SearchOrder getOrder() {
        return order;
    }
}
