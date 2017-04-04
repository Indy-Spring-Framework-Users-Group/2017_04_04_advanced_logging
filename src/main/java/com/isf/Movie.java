package com.isf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class Movie {
    private String title;
    private int year;
    private String rating;

    public static Movie from(Map httpResponse) {
        return new Movie(httpResponse.get("Title").toString(), Integer.valueOf(httpResponse.get("Year").toString()), httpResponse.get("Rated").toString());
    }
}
