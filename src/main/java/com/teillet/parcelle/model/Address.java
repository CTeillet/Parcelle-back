package com.teillet.parcelle.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

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

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "address_plot_code", joinColumns = @JoinColumn(name = "address_id"))
	@Column(name = "plot_id")
	private List<String> plotIds;

	@ManyToOne(optional = false)
	@JoinColumn(name = "insee_city_name", nullable = false)
	private City city;

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
