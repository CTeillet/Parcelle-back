package com.teillet.parcelle.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.locationtech.jts.geom.Polygon;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "limitation")
public class Border {
    @Column(name = "id")
    @Id
    @Generated
    private String id;

    @Column(name = "geom", columnDefinition = "geometry(Polygon, 4326)")
    private Polygon geom;

    @Column(name = "date-mise-a-jour")
    private LocalDateTime dateMiseAJour;
}
