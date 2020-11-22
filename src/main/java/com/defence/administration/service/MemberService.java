package com.defence.administration.service;

import com.defence.administration.dto.MemberDto;
import com.defence.administration.model.Member;
import com.defence.administration.repository.MemberRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    
    @Autowired
    MemberRepository repository;
    
    @Autowired
    TrainingService trainingService;
    
    public List <Member> findAll(){
        return repository.findAll();
    }
    
    public Optional <Member> findById(Long id){
        return repository.findById(id);
    }
    
    public Member saveAndFlush(Member member){
       return repository.saveAndFlush(member);
    }
    
    public void delete(Member member){
        member.setTrainings(new HashSet<>());
        repository.delete(saveAndFlush(member));
    }
    
    public void update(Member member, MemberDto dto){
        member.setFirstName(dto.getFirstName());
        member.setMiddleName(dto.getMiddleName());
        member.setLastName(dto.getLastName());
        member.setPaidUntil(dto.getPaidUntil());
        member.setIBAN(dto.getIBAN());
        member.setStreetWithNr(dto.getStreetWithNr());
        member.setZipCode(dto.getZipCode());
        member.setResidence(dto.getResidence());
        member.setDateOfBirth(dto.getDateOfBirth());
        member.setPhoneHome(dto.getPhoneHome());
        member.setPhoneMobile(dto.getPhoneMobile());
        member.setEmail(dto.getEmail());
        member.setIsTrainer(dto.getIsTrainer());
        member.setIsSender(dto.getIsSender());
        member.setImage(dto.getImage());
        saveAndFlush(member);
    }
    
    public Member map(MemberDto dto){
        Member member = new Member();
        member.setId(dto.getId());
        member.setFirstName(dto.getFirstName());
        member.setMiddleName(dto.getMiddleName());
        member.setLastName(dto.getLastName());
        member.setPaidUntil(dto.getPaidUntil());
        member.setIBAN(dto.getIBAN());
        member.setStreetWithNr(dto.getStreetWithNr());
        member.setZipCode(dto.getZipCode());
        member.setResidence(dto.getResidence());
        member.setDateOfBirth(dto.getDateOfBirth());
        member.setPhoneHome(dto.getPhoneHome());
        member.setPhoneMobile(dto.getPhoneMobile());
        member.setEmail(dto.getEmail());
        member.setIsTrainer(dto.getIsTrainer());
        member.setIsSender(dto.getIsSender());
        member.setImage(dto.getImage());
       return member;
    }
    
    public MemberDto map(Member member){
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setMiddleName(member.getMiddleName());
        dto.setLastName(member.getLastName());
        dto.setPaidUntil(member.getPaidUntil());
        dto.setIBAN(member.getIBAN());
        dto.setStreetWithNr(member.getStreetWithNr());
        dto.setZipCode(member.getZipCode());
        dto.setResidence(member.getResidence());
        dto.setDateOfBirth(member.getDateOfBirth());
        dto.setPhoneHome(member.getPhoneHome());
        dto.setPhoneMobile(member.getPhoneMobile());
        dto.setEmail(member.getEmail());
        dto.setIsTrainer(member.getIsTrainer());
        dto.setIsSender(member.getIsSender());
        dto.setImage(member.getImage());
        return dto;
    }
    
}
