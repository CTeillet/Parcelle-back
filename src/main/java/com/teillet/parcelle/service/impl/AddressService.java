package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.repository.AdresseRepository;
import com.teillet.parcelle.service.IAddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService implements IAddressService {

	private final AdresseRepository adresseRepository;

	@Override
	public Long addressNumber() {
		return adresseRepository.count();
	}

	@Override
	public void saveAddress(Address address) {
		adresseRepository.save(address);
	}

	@Override
	public Address getAddressByPlotId(String id) {
		return adresseRepository.findByCodesParcellesContains(id);
	}

	@Override
	public List<String> getAddressTypes() {
		return adresseRepository.findDistinctDestinationPrincipale();
	}

}
