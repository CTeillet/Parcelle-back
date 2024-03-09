package com.teillet.parcelle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class City {
	@Id
	String inseeCityCode;
	String zipCode;
	String cityName;
	String countyName;
	String nomReg;

	@OneToMany(mappedBy = "city")
	@ToString.Exclude
	List<Plot> plots;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		City city = (City) o;
		return getInseeCityCode() != null && Objects.equals(getInseeCityCode(), city.getInseeCityCode());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
