package com.turbo.model.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ReindexAction {
    private long id;

    @JsonIgnore
    public abstract ReindexAction merge(ReindexAction newValue);

    @JsonIgnore
    public abstract String getType();

    @JsonIgnore
    public <T> T getOriginalValue() {
        return (T)this;
    }
}
