package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Address;

import java.util.List;

public interface IAddressService {
	Long addressNumber();

	void saveAddress(Address address);

	Address getAddressByPlotId(String id);

	List<String> getAddressTypes();
}
