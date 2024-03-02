package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {
	//Recuperer l'adresse correspondant à une parcelle en triant par ordre croissant d'adresse_id et en gardant le plus petit
	@Query(value = "SELECT * FROM address WHERE id IN (SELECT address_id FROM address_plot_code WHERE plot_id = ?1) ORDER BY id LIMIT 1", nativeQuery = true)
	Address findByCodesParcellesContains(@NonNull String codesParcelles);

	//Recuperation des differentes valeur de destination_principal
	@Query(value = "Select distinct a.main_destination from address a RIGHT JOIN plot p on a.id = p.address_id ORDER BY main_destination", nativeQuery = true)
	List<String> findDistinctDestinationPrincipale();

}
