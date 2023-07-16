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
@Table(name = "adresse")
public class Adresse {
	@Id
	private String id;
	private String codeVoie;
	private String numeroComplet;
	private String numero;
	private String repetition;
	private boolean pseudoNumero;
	private String nomVoie;
	private String nomVoieOriginal;
	private String nomVoieType;
	private String nomVoieFantoir;
	private String destinationPrincipale;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "adresse_code_parcelle", joinColumns = @JoinColumn(name = "adresse_id"))
	@Column(name = "code_parcelle")
	private List<String> codesParcelles;

	@ManyToOne(optional = false)
	@JoinColumn(name = "commune_insee_com", nullable = false)
	private Commune commune;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Adresse adresse = (Adresse) o;
		return getId() != null && Objects.equals(getId(), adresse.getId());
	}

	@Override
	public String toString() {
		return numeroComplet + " " + nomVoieOriginal + ", " + commune.getNomCom() + " " + commune.getCodePostal();
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
