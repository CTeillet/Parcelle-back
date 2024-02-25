package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AdresseRepository extends JpaRepository<Address, String> {
	//Recuperer l'adresse correspondant à une parcelle en triant par ordre croissant d'adresse_id et en gardant le plus petit
	@Query(value = "SELECT * FROM adresse WHERE id IN (SELECT adresse_id FROM adresse_code_parcelle WHERE code_parcelle = ?1) ORDER BY id LIMIT 1", nativeQuery = true)
	Address findByCodesParcellesContains(@NonNull String codesParcelles);

	//Recuperation des differentes valeur de destination_principal
	@Query(value = "Select distinct a.destination_principale from adresse a RIGHT JOIN public.parcelle p on a.id = p.adresse_id ORDER BY destination_principale", nativeQuery = true)
	List<String> findDistinctDestinationPrincipale();

}
