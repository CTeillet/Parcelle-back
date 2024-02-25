package com.teillet.parcelle.controller;

import com.teillet.parcelle.service.IAddressService;
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
public class AddressController {
	private final IAddressService addressService;

	@GetMapping("/type")
	public ResponseEntity<List<String>> recuperationTypeAdresse() {
		log.info("Récupération des types d'adresse");
		List<String> typeParcelles = addressService.getAddressTypes();
		return ResponseEntity.ok(typeParcelles);
	}


}
