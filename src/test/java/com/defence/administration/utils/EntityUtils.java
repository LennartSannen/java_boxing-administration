package com.defence.administration.utils;

import com.defence.administration.enums.TrainingType;
import com.defence.administration.model.Member;
import com.defence.administration.model.Training;
import com.defence.administration.model.User;
import com.defence.administration.repository.MemberRepository;
import com.defence.administration.repository.TrainingRepository;
import com.defence.administration.repository.UserRepository;
import java.time.LocalDate;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class EntityUtils {
    
    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrainingRepository trainingRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Member createMember() {
        Member member = new Member();
        member.setFirstName("Bram");
        member.setMiddleName(null);
        member.setLastName("Timmermans");
        member.setPaidUntil(LocalDate.now());
        member.setIBAN("NL23RABO383383838");
        member.setStreetWithNr("Straat 9");
        member.setZipCode("5211AX");
        member.setResidence("Boxtel");
        member.setDateOfBirth(LocalDate.of(1995, 12, 22));
        member.setPhoneHome(null);
        member.setPhoneMobile(68864864L);
        member.setEmail("bram@abc.com");
        return memberRepository.saveAndFlush(member);
    }

    public Member createMember(Long trainingId) {
        Training training = trainingRepository.findById(trainingId).get();
        Member member = createMember();
        training.getMembers().add(member);
        trainingRepository.saveAndFlush(training);
        return member;
    }

    public Training createTraining() {
        Training training = new Training();
        training.setDate(LocalDate.now());
        training.setTrainerName("J. Schellekens");
        training.setSenderName("B. Timmermans");
        training.setType(TrainingType.BOKSEN);
        return trainingRepository.saveAndFlush(training);
    }
    
    public User createUser() {
        User user = new User();
        user.setUsername("Bram123");
        user.setPassword(bCryptPasswordEncoder.encode("fedcba"));
        return userRepository.saveAndFlush(user);
    }

}
