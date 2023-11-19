
package ru.anime.okami.generatedPojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;
import ru.anime.okami.utils.ListToStringConverter;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "anime_title",
        "title_en",
        "other_titles",
        "other_titles_en",
        "other_titles_jp",
        "anime_kind",
        "all_status",
        "anime_status",
        "description",
        "anime_description",
        "poster_url",
        "screenshots",
        "duration",
        "all_genres",
        "anime_genres",
        "anime_studios",
        "shikimori_rating",
        "shikimori_votes",
        "aired_at",
        "next_episode_at",
        "rating_mpaa",
        "episodes_total",
        "episodes_aired"
})
@Data
@Entity
public class MaterialData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("title")
    @Column(length = 10000)
    private String title;

    @JsonProperty("anime_title")
    @Column(length = 10000)
    private String animeTitle;

    @JsonProperty("title_en")
    @Column(length = 10000)
    private String titleEn;

    @JsonProperty("other_titles")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> otherTitles;

    @JsonProperty("other_titles_en")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> otherTitlesEn;

    @JsonProperty("other_titles_jp")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> otherTitlesJp;

    @JsonProperty("anime_kind")
    private String animeKind;

    @JsonProperty("all_status")
    private String allStatus;

    @JsonProperty("anime_status")
    private String animeStatus;

    @JsonProperty("description")
    @Column(length = 10000)
    private String description;

    @JsonProperty("anime_description")
    @Column(length = 10000)
    private String animeDescription;

    @JsonProperty("poster_url")
    @Column(length = 10000)
    private String posterUrl;

    @JsonProperty("screenshots")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> screenshots;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("all_genres")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> allGenres;

    @JsonProperty("anime_genres")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> animeGenres;

    @JsonProperty("anime_studios")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private List<String> animeStudios;

    @JsonProperty("shikimori_rating")
    private double shikimoriRating;

    @JsonProperty("shikimori_votes")
    private int shikimoriVotes;

    @JsonProperty("aired_at")
    @Column(length = 10000)
    private String airedAt;

    @JsonProperty("next_episode_at")
    @Column(length = 10000)
    private String nextEpisodeAt;

    @JsonProperty("rating_mpaa")
    private String ratingMpaa;

    @JsonProperty("episodes_total")
    private int episodesTotal;

    @JsonProperty("episodes_aired")
    private int episodesAired;

    @JsonIgnore
    private final static long serialVersionUID = -2086467949456573911L;

}
