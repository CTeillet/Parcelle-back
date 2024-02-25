package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.repository.TownRepository;
import com.teillet.parcelle.service.ITownService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TownService implements ITownService {
    private final TownRepository townRepository;

    @Override
    public Long townNumber() {
        return townRepository.count();
    }

    @Override
    @Transactional
    public List<Commune> saveTowns(List<Commune> communes) {
        return townRepository.saveAll(communes);
    }
}
