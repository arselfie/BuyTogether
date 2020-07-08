package com.project.demo.services;

import com.project.demo.entity.Address;
import com.project.demo.entity.User;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.AddressRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

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


        return addressRepository.save(address);
    }


}
