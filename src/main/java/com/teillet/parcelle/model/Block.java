package com.teillet.parcelle.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "pate")
public class Block {
	@Id
	@Generated
	private Long id;

	@ManyToOne
	@JoinColumn(name = "territoire_id")
	private Territoire territoire;

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
