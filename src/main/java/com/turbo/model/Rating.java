package com.turbo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating implements Serializable {

    private long ups;
    private long downs;
    private long rating; // upvotes - downvotes

    public void change(long ratingChangeAmount) {
        if (ratingChangeAmount > 0) {
            ups += ratingChangeAmount;
        } else {
            downs += ratingChangeAmount;
        }
        rating += ratingChangeAmount;
    }
}
