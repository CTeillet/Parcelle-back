package com.teillet.parcelle.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "address")
public class Address {
	@Id
	private String id;
	private String codeVoie;
	private String fullNumber;
	private String number;
	private String repetition;
	private boolean pseudoNumber;
	private String laneName;
	private String originalNameLane;
	private String laneNameType;
	private String laneNameFantoir;
	private String mainDestination;

	@ManyToOne(optional = false)
	@JoinColumn(name = "insee_city_name", nullable = false)
	private City city;

	@ManyToMany
	@JoinTable(name = "address_plots",
			joinColumns = @JoinColumn(name = "address_id"),
			inverseJoinColumns = @JoinColumn(name = "plots_id"))
	private Set<Plot> plots = new LinkedHashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Address adresse = (Address) o;
		return getId() != null && Objects.equals(getId(), adresse.getId());
	}

	@Override
	public String toString() {
		return fullNumber + " " + originalNameLane + ", " + city.getCityName() + " " + city.getZipCode();
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
