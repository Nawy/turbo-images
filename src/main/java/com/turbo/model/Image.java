package com.turbo.model;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by ermolaev on 5/6/17.
 */
@Table
public class Image {
    @PrimaryKey
    private long id;
    private String name;
    private String description;
    private long upVote;
    private long downVote;
    private String path;

    public Image() {
    }

    public Image(long id, String name, String description, long upVote, long downVote, String path) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.upVote = upVote;
        this.downVote = downVote;
        this.path = path;
    }
}
