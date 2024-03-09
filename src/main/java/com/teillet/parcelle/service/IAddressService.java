package com.teillet.parcelle.service;

import com.teillet.parcelle.model.Address;

import java.util.List;

public interface IAddressService {
	Long addressNumber();

	List<String> getAddressTypes();

	void saveAddresses(List<Address> addresses);

}
