package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Adresse;

public interface IAdresseService {
	Long nombreAdresse();

	void enregistrementAdresse(Adresse adresse);

	Adresse recuperationAdresseCorrespondantParcelle(String id);
}
