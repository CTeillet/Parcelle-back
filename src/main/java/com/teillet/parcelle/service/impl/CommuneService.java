package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Commune;
import com.teillet.parcelle.repository.CommuneRepository;
import com.teillet.parcelle.service.ICommuneService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommuneService implements ICommuneService {
    private final CommuneRepository communeRepository;

    @Override
    public Long nombreCommune() {
        return communeRepository.count();
    }

    @Override
    @Transactional
    public List<Commune> enregistrementLotCommune(List<Commune> communes) {
        return communeRepository.saveAll(communes);
    }
}
