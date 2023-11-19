
package ru.anime.okami.generatedPojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "total",
        "prev_page",
        "next_page",
        "results"
})
@lombok.Data
@Entity
public class List implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("time")
    private String time;

    @JsonProperty("total")
    private int total;

    @JsonProperty("prev_page")
    private String prevPage;

    @JsonProperty("next_page")
    private String nextPage;

    @JsonProperty("results")
    @OneToMany
    @JoinColumn(name = "results_id")
    private java.util.List<Results> results;

    @Serial
    private final static long serialVersionUID = -4113027694352885317L;
}
