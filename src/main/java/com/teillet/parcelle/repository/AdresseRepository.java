package com.teillet.parcelle.repository;

import com.teillet.parcelle.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface AdresseRepository extends JpaRepository<Adresse, String> {
	//Adresse findByCodesParcellesIn(Collection<String> codesParcelles);
	//Adresse findByCodesParcellesContains(@NonNull String codesParcelles);

	//Recuperer l'adresse correspondant à une parcelle en triant par ordre croissant d'adresse_id et en gardant le plus petit
	@Query(value = "SELECT * FROM adresse WHERE id IN (SELECT adresse_id FROM adresse_code_parcelle WHERE code_parcelle = ?1) ORDER BY id LIMIT 1", nativeQuery = true)
	Adresse findByCodesParcellesContains(@NonNull String codesParcelles);

}
