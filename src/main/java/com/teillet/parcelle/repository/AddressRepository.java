package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {

	//Recuperation des differentes valeur de destination_principal
	@Query(value = "Select distinct a.main_destination from address a ORDER BY main_destination", nativeQuery = true)
	List<String> findDistinctDestinationPrincipale();

}
