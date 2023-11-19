
package ru.anime.okami.generatedPojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import ru.anime.okami.utils.ListToStringConverter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "type",
        "link",
        "title",
        "title_orig",
        "other_title",
        "translation",
        "year",
        "last_season",
        "last_episode",
        "episodes_count",
        "imdb_id",
        "worldart_link",
        "shikimori_id",
        "quality",
        "camrip",
        "lgbt",
        "blocked_countries",
        "created_at",
        "updated_at",
        "material_data",
        "screenshots"
})
@lombok.Data
@Entity
public class Results implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long animeId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("link")
    @Column(length = 10000)
    private String link;

    @JsonProperty("title")
    @Column(length = 10000)
    private String title;

    @JsonProperty("title_orig")
    @Column(length = 10000)
    private String titleOrig;

    @JsonProperty("other_title")
    @Column(length = 10000)
    private String otherTitle;

    @JsonProperty("translation")
    @OneToOne
    private Translation translation;

    @JsonProperty("year")
    private int year;

    @JsonProperty("last_season")
    private int lastSeason;

    @JsonProperty("last_episode")
    private int lastEpisode;

    @JsonProperty("episodes_count")
    private int episodesCount;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("worldart_link")
    @Column(length = 10000)
    private String worldartLink;

    @JsonProperty("shikimori_id")
    private String shikimoriId;

    @JsonProperty("quality")
    @Column(length = 10000)
    private String quality;

    @JsonProperty("camrip")
    private boolean camrip;

    @JsonProperty("lgbt")
    private boolean lgbt;

    @JsonProperty("blocked_countries")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private java.util.List<String> blockedCountries;

    @JsonProperty("created_at")
    @Column(length = 10000)
    private String createdAt;

    @JsonProperty("updated_at")
    @Column(length = 10000)
    private String updatedAt;

    @JsonProperty("material_data")
    @OneToOne(cascade = CascadeType.ALL)
    private MaterialData materialData;

    @JsonProperty("screenshots")
    @Convert(converter = ListToStringConverter.class)
    @Column(length = 10000)
    private java.util.List<String> screenshots;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "all_translations", length = 10000)
    private java.util.List<String> allTranslations;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "list_id")
    private List list;

    private final static long serialVersionUID = 5385869221142683370L;
}
