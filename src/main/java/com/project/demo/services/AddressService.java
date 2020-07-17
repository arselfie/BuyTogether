package com.project.demo.services;

import com.project.demo.entity.Address;
import com.project.demo.entity.Item;
import com.project.demo.entity.User;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.AddressRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Address> find() {
        return addressRepository.findAll();
    }

    public Address find(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public Address createAddress(Address address) {
        if (address == null) {
            throw new ValidationException("Address is null");
        }
        if (address.getCustomer() == null) {
            throw new ValidationException("Customer is null");
        }
        Long userID = address.getCustomer().getId();
        User userFromDb = userRepository.findById(userID).orElse(null);

        if (userFromDb == null) {
            throw new ValidationException("Customer with id " + userID + " not exist");
        }

        if (address.getCity() == null) {
            throw new ValidationException("City is null");
        }
        if (address.getCity().length() < 3) {
            throw new ValidationException("City name is too short");
        }
        if (address.getStreet() == null) {
            throw new ValidationException("Street is null");
        }
        if (address.getStreet().length() < 3) {
            throw new ValidationException("Street name is too short");
        }
        if (address.getPostalCode() == null) {
            throw new ValidationException("Postal code is null");
        }
        //W
        if (address.getPostalCode() < 0 || address.getPostalCode() > 100000) {
            throw new ValidationException("Postal code is not valid");
        }

        //here
        //return houses.stream().filter(h -> h.getName().equals(houseName)).findFirst().get();

        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, Address address) {
        Address existedAddress = find(id);

        if (existedAddress != null) {
            existedAddress.setStreet(address.getStreet());
            existedAddress.setCity(address.getCity());
            existedAddress.setPostalCode(address.getPostalCode());
            addressRepository.save(existedAddress);

        }
        return existedAddress;
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }




}
