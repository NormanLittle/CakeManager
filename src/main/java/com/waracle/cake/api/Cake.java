package com.waracle.cake.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cake {

    private final Integer id;
    private final String title;
    private final String description;
    private final String image;

    @JsonCreator
    public Cake(@JsonProperty("id") Integer id,
                @JsonProperty("title") String title,
                @JsonProperty("description") String description,
                @JsonProperty("image") String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
