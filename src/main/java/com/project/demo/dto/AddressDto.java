package com.project.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.demo.entity.address.Address;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {


    private Long id;

    private String street;

    private String city;

    private Integer postalCode;

    public AddressDto() {
    }

    public AddressDto(Address address) {
        this.id = address.getId();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.postalCode = address.getPostalCode();

    }


    public Address toEntity() {
        Address address = new Address();
        address.setId(id);
        address.setStreet(street);
        address.setCity(city);
        address.setPostalCode(postalCode);

        return address;
    }


}
