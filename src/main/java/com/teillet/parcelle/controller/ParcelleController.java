package com.teillet.parcelle.controller;

import com.teillet.parcelle.model.Parcelle;
import com.teillet.parcelle.service.IParcelleService;
import com.teillet.parcelle.utils.ParcelleUtils;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class ParcelleController {
	private final IParcelleService parcelleService;

	@GetMapping("/parcelle")
	@CrossOrigin(origins = "http://localhost:4200")
	@Timed(value = "parcelleController.getParcelle", description = "Time taken to return all the parcelles")
	public ResponseEntity<String> getParcelle() throws IOException {
		log.info("Récupération des parcelles");
		List<Parcelle> parcelles = parcelleService.recuperationParcellesNonSupprimees();
		String string = ParcelleUtils.parcellesToGeoJson(parcelles);
		log.info("Récupération des parcelles terminée");
		return ResponseEntity.ok(string);
	}

	//Delete a list of parcelles by id of String
	@DeleteMapping("/parcelle")
	@CrossOrigin(origins = "http://localhost:4200")
	@Timed(value = "parcelleController.deleteParcelles", description = "Time taken to delete a list of parcelle")
	public ResponseEntity<String> deleteParcelles(@RequestBody List<String> ids) {
		log.info("Suppression des parcelles " + ids);
		if ( parcelleService.suppressionParcelles(ids) ) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	//Get one parcelle
	@GetMapping("/parcelle/{id}")
	@CrossOrigin(origins = "http://localhost:4200")
	@Timed(value = "parcelleController.getParcelleById", description = "Time taken to return a parcelle")
	public ResponseEntity<String > getParcelleById(@PathVariable String id) throws IOException {
		log.info("Récupération de la parcelle " + id);
		Parcelle parcelle = parcelleService.recuperationParcelleParId(id);
		String string = ParcelleUtils.parcelleToGeoJson(parcelle);
		return ResponseEntity.ok(string);
	}

}
