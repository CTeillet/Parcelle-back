package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneRepository extends JpaRepository<Commune, String> {
}
