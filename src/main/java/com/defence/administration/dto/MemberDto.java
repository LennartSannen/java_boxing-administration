package com.defence.administration.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MemberDto {
    
    private Long id;
    @NotBlank
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate paidUntil;
    private String IBAN;
    private String streetWithNr;
    private String zipCode;
    private String residence;
    private LocalDate dateOfBirth;
    private Long phoneHome;
    private Long phoneMobile;
    private String email;
    @NotNull
    private Boolean isTrainer = false;
    @NotNull
    private Boolean isSender = false;
    private byte[] image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getPaidUntil() {
        return paidUntil;
    }

    public void setPaidUntil(LocalDate paidUntil) {
        this.paidUntil = paidUntil;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getStreetWithNr() {
        return streetWithNr;
    }

    public void setStreetWithNr(String streetWithNr) {
        this.streetWithNr = streetWithNr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(Long phoneHome) {
        this.phoneHome = phoneHome;
    }

    public Long getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(Long phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsTrainer() {
        return isTrainer;
    }

    public void setIsTrainer(Boolean isTrainer) {
        this.isTrainer = isTrainer;
    }

    public Boolean getIsSender() {
        return isSender;
    }

    public void setIsSender(Boolean isSender) {
        this.isSender = isSender;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
}
