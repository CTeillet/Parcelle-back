package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.City;
import com.teillet.parcelle.repository.CityRepository;
import com.teillet.parcelle.service.ITownService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TownService implements ITownService {
    private final CityRepository cityRepository;

    @Override
    public Long townNumber() {
        return cityRepository.count();
    }

    @Override
    //@Transactional
    public List<City> saveTowns(List<City> cities) {
        return cityRepository.saveAll(cities);
    }
}
