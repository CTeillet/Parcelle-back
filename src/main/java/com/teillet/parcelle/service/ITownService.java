package com.teillet.parcelle.service;

import com.teillet.parcelle.model.City;

import java.util.List;

public interface ITownService {
	Long townNumber();
	List<City> saveTowns(List<City> cities);
}
