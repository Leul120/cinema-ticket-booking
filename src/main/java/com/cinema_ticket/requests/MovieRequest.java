package com.cinema_ticket.requests;

import com.cinema_ticket.entities.Image;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.Year;
import java.util.Date;
import java.util.List;

@Data
public class MovieRequest {
    private String title;
    private Image poster;
    private String description;
    private String genre;
    private long duration;
    private String language;
    private List<String> writer;
    private String director;
    private String country;
    private Year year;
    private String trailer;
    private List<String> actors;
    private Boolean subtitle_availability;
    private String age_rating;
    private List<Image> images;
    private Date release_date;
    private Date created_at;
    private Date updated_at;
}
