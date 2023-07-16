package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Parcelle;

import java.util.List;

public interface IParcelleService {
	Long nombreParcelle();

	List<Parcelle> enregistrementLotParcelle(List<Parcelle> parcelles);

	boolean suppressionParcelles(List<String> ids);

	List<Parcelle> recuperationParcellesNonSupprimees();

	Parcelle recuperationParcelleParId(String id);

	List<Parcelle> recuperationParcellesNonLieesAdresse();

	void enregistrementParcelle(Parcelle parcelle);

	Long nombreParcelleLieesAdresse();

}
