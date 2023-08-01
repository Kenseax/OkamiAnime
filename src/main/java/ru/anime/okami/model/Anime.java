package ru.anime.okami.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String altNames;

    private String description;

    private int amountOfEpisodes;

    private String year;

    private String season;

    private String type;

    private String status;

    private String poster;

    private double rating;

    @ManyToMany(mappedBy = "animeList")
    private List<Genre> genres;

    @OneToMany(mappedBy = "animeComments")
    private List<Comment> comment;

    @OneToOne(mappedBy = "animeEpisodes")
    private Episode episode;

}
