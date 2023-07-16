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
public class Commune {

	@Id
	String inseeCom;

	String codePostal;

	String nomCom;

	String nomDept;

	String nomReg;

	@OneToMany(mappedBy = "commune")
	@ToString.Exclude
	List<Parcelle> parcelles;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Commune commune = (Commune) o;
		return getInseeCom() != null && Objects.equals(getInseeCom(), commune.getInseeCom());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
