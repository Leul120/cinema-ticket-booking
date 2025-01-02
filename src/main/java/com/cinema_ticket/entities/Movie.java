package com.cinema_ticket.entities;

import com.cinema_ticket.utils.StringListConverter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.Year;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Movie  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @OneToOne
    private Image poster;
    private String description;
    private String genre;
    private Year year;
    private long duration;
    private String language;
    private Boolean subtitle_availability;
    private String age_rating;
//    @JsonManagedReference(value = "movie-image")
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Image> images;
    private String director;
    private String country;
    private Date release_date;
    private String trailer;
    @Convert(converter = StringListConverter.class)
    private List<String> actors;
    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews;
    @Convert(converter = StringListConverter.class)
    private List<String> writer;
    private Date created_at;
    private Date updated_at;


}
