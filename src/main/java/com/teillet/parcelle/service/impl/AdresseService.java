package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Adresse;
import com.teillet.parcelle.repository.AdresseRepository;
import com.teillet.parcelle.service.IAdresseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdresseService implements IAdresseService {

	private final AdresseRepository adresseRepository;

	@Override
	public Long nombreAdresse() {
		return adresseRepository.count();
	}

	@Override
	public void enregistrementAdresse(Adresse adresse) {
		adresseRepository.save(adresse);
	}

	@Override
	public Adresse recuperationAdresseCorrespondantParcelle(String id) {
		return adresseRepository.findByCodesParcellesContains(id);
	}

	@Override
	public List<String> recuperationValeursDestinationPrincipale() {
		return adresseRepository.findDistinctDestinationPrincipale();
	}

}
