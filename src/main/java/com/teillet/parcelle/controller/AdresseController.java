package com.teillet.parcelle.controller;

import com.teillet.parcelle.service.IAdresseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/adresse")
public class AdresseController {
	private final IAdresseService adresseService;

	@GetMapping("/type")
	public ResponseEntity<List<String>> recuperationTypeAdresse() {
		log.info("Récupération des types d'adresse");
		List<String> typeParcelles = adresseService.recuperationValeursDestinationPrincipale();
		return ResponseEntity.ok(typeParcelles);
	}


}
