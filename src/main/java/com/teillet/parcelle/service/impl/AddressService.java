package com.teillet.parcelle.service.impl;

import com.teillet.parcelle.model.Address;
import com.teillet.parcelle.repository.AddressRepository;
import com.teillet.parcelle.service.IAddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService implements IAddressService {

	private final AddressRepository addressRepository;

	@Override
	public Long addressNumber() {
		return addressRepository.count();
	}

	@Override
	public List<String> getAddressTypes() {
		return addressRepository.findDistinctDestinationPrincipale();
	}

	@Override
	public void saveAddresses(List<Address> addresses) {
		addressRepository.saveAll(addresses);
	}

}
