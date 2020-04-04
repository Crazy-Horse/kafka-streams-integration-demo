package com.csv.integration.kafka.streams.demo.data;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String countryCode;
    private String phoneNumber;
    @Enumerated(STRING)
    private PhoneType phoneType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static enum PhoneType {
        Home,
        Business,
        Mobile,
        Fax
    }

    public Phone() {}

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "countryCode='" + countryCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneType=" + phoneType +
                '}';
    }
}
