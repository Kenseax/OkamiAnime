package ru.anime.okami.generatedPojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "translations")
public class Translation {

    @Id
    private long id;

    private String title;

    private String type;

    @OneToOne
    private Results results;
}
