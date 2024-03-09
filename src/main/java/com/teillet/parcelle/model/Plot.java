package com.teillet.parcelle.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.locationtech.jts.geom.Polygon;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "plot", schema = "public", indexes = {
		@Index(name = "plot_pkey", columnList = "id", unique = true),
		@Index(name = "plot_insee_city_name_idx", columnList = "insee_city_name"),
		@Index(name = "plot_block_id_idx", columnList = "block_id"),
		@Index(name = "plot_deleted_idx", columnList = "deleted")
})
public class Plot {
	@Id
	private String id;

	@Column(name = "geom", columnDefinition = "geometry(Polygon, 4326)")
	private Polygon geom;

	@Column(name = "surface")
	private Integer surface;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "insee_city_name", nullable = false)
	private City city;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "block_id")
	private Block block;

	//set default value to false
	@Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean deleted;

	@ToString.Exclude
	@ManyToMany(mappedBy = "plots")
	private Set<Address> addresses = new LinkedHashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Plot plot = (Plot) o;
		return getId() != null && Objects.equals(getId(), plot.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
