package com.defence.administration.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_id_generator")
    @SequenceGenerator(name="member_id_generator", sequenceName = "member_id_seq", allocationSize=1)
    private Long id;
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
    private Boolean isTrainer = false;
    private Boolean isSender = false;
    @Lob
    private byte[] image;
    @ManyToMany(mappedBy = "members")
    private Set <Training> trainings = new HashSet<>();

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
    
    public void setTrainings(Set<Training> trainings) {
        this.trainings = trainings;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
