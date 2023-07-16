package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Commune;

import java.util.List;

public interface ICommuneService {
	Long nombreCommune();
	List<Commune> enregistrementLotCommune(List<Commune> communes);
}
