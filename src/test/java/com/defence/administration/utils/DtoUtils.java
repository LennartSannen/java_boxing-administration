package com.defence.administration.utils;

import com.defence.administration.dto.MemberDto;
import com.defence.administration.dto.TrainingDto;
import com.defence.administration.dto.UserDto;
import com.defence.administration.enums.TrainingType;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DtoUtils {
    
    public MemberDto createMemberDto(){
        MemberDto dto = new MemberDto();
        dto.setFirstName("Bram");
        dto.setMiddleName(null);
        dto.setLastName("Timmermans");
        dto.setPaidUntil(LocalDate.now());
        dto.setIBAN("NL23RABO34223");
        dto.setStreetWithNr("Kerkstraat 9");
        dto.setZipCode("5231AX");
        dto.setResidence("Boxtel");
        dto.setDateOfBirth(LocalDate.of(1995,12,22));
        dto.setPhoneHome(null);
        dto.setPhoneMobile(6564685L);
        dto.setEmail("bram@hotmail.com");
        return dto;
    }
    
    public TrainingDto createTrainingDto(){
        TrainingDto dto = new TrainingDto();
        dto.setDate(LocalDate.now());
        dto.setTrainerName("J. Schellekens");
        dto.setSenderName("B. Timmermans");
        dto.setType(TrainingType.BOKSEN);
        return dto;
    }
    
    public UserDto createUserDto() {
        UserDto dto = new UserDto();
        dto.setUsername("Bram1");
        dto.setPassword("abcdef");
        return dto;
    }
    
}
