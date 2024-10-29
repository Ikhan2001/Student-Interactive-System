package com.cleducate.entity;


import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address extends BaseEntity{

    private String city;
    private String stateProvince;
    private String zipCode;
    private String country;


}
