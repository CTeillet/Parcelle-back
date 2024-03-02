package com.teillet.parcelle.model;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Polygon;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Border {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "geom", columnDefinition = "geometry(Polygon, 4326)")
    private Polygon geom;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
