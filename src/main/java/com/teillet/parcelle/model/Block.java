package com.teillet.parcelle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "block")
public class Block {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "geom", columnDefinition = "geometry(Polygon, 4326)")
	private Polygon geom;

	@ManyToOne
	@JoinColumn(name = "territory_id")
	private Territory territory;

	@OneToMany(mappedBy = "block", orphanRemoval = true)
	@ToString.Exclude
	private List<Plot> plots = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Block block = (Block) o;
		return getId() != null && Objects.equals(getId(), block.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
