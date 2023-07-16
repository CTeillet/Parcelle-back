package com.teillet.parcelle.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.locationtech.jts.geom.Polygon;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "parcelle", schema = "public", indexes = {
		@Index(name = "parcelle_pkey", columnList = "id", unique = true),
		@Index(name = "parcelle_commune_insee_com_idx", columnList = "commune_insee_com"),
		@Index(name = "parcelle_pate_id_idx", columnList = "pate_id"),
		@Index(name = "parcelle_adresse_id_idx", columnList = "adresse_id"),
		@Index(name = "parcelle_supprime_idx", columnList = "supprime")
})
public class Parcelle {
	@Id
	private String id;

	@Column(name = "geom", columnDefinition = "geometry(Polygon, 4326)")
	private Polygon geom;

	@Column(name = "surface")
	private Integer surface;

	@ManyToOne(optional = false)
	@JoinColumn(name = "commune_insee_com", nullable = false)
	private Commune commune;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "pate_id")
	private Pate pate;

	//set default value to false
	@Column(name = "supprime", nullable = false, columnDefinition = "boolean default false")
	private Boolean supprime;

	@ManyToOne
	@JoinColumn(name = "adresse_id")
	private Adresse adresse;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Parcelle parcelle = (Parcelle) o;
		return getId() != null && Objects.equals(getId(), parcelle.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
