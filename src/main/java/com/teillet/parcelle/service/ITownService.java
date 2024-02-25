package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Commune;

import java.util.List;

public interface ITownService {
	Long townNumber();
	List<Commune> saveTowns(List<Commune> communes);
}
