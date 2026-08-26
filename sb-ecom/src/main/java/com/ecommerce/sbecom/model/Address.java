package com.ecommerce.sbecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "street name must be at least 5 characters...")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters...")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "city name must be at least 4 characters...")
    private String city;


    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters...")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters...")
    private String country;

    @NotBlank
    @Size(min = 6, message = " pinCode must be at least 6 characters...")
    private String pinCode;


    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();


    public Address(String buildingName, String city, String country, String street, String state, String pinCode) {
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
        this.street = street;
        this.state = state;
        this.pinCode = pinCode;
    }
}
