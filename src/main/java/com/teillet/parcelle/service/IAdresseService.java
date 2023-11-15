package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Adresse;

import java.util.List;

public interface IAdresseService {
	Long nombreAdresse();

	void enregistrementAdresse(Adresse adresse);

	Adresse recuperationAdresseCorrespondantParcelle(String id);

	List<String> recuperationValeursDestinationPrincipale();
}
