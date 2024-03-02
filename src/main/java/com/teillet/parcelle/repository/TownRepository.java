package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<City, String> {
}
