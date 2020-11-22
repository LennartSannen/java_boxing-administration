package com.defence.administration.service;

import com.defence.administration.dto.TrainingDto;
import com.defence.administration.model.Member;
import com.defence.administration.model.Training;
import com.defence.administration.repository.TrainingRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    @Autowired
    TrainingRepository repository;

    @Autowired
    MemberService memberService;

    public List<Training> findAll() {
        return repository.findAll();
    }

    public Optional<Training> findById(Long id) {
        return repository.findById(id);
    }

    public Training saveAndFlush(Training training) {
        return repository.saveAndFlush(training);
    }

    public void delete(Training training) {
        training.setMembers(new HashSet<>());
        repository.delete(saveAndFlush(training));
    }

    public Optional findMemberById(Long id) {
        return memberService.findById(id);
    }

    public void update(Training entity, TrainingDto dto) {
        entity.setDate(dto.getDate());
        entity.setMembers(dto.getMembers().stream()
                .filter(m -> memberService
                        .findById(m.getId())
                        .isPresent())
                .map(memberService::map)
                .collect(Collectors.toSet()));
        entity.setTrainerName(dto.getTrainerName());
        entity.setSenderName(dto.getSenderName());
        entity.setType(dto.getType());
        saveAndFlush(entity);
    }

    public List<TrainingDto> findTrainingsOfMember(Member member) {
        return findAll().stream()
                .filter(t -> t.getMembers().stream()
                .anyMatch(m -> m.getId().equals(member.getId())))
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Training map(TrainingDto dto) {
        Training training = new Training();
        training.setId(dto.getId());
        training.setDate(dto.getDate());
        training.getMembers().addAll(dto.getMembers().stream()
                .map(m -> memberService.findById(m.getId()))
                .filter(m -> m.isPresent())
                .map(Optional::get)
                .collect(Collectors.toSet()));
        training.setTrainerName(dto.getTrainerName());
        training.setSenderName(dto.getSenderName());
        training.setType(dto.getType());
        return training;
    }

    public TrainingDto map(Training training) {
        TrainingDto dto = new TrainingDto();
        dto.setId(training.getId());
        dto.setDate(training.getDate());
        dto.getMembers().addAll(training.getMembers().stream()
                .map(memberService::map)
                .collect(Collectors.toSet()));
        dto.setTrainerName(training.getTrainerName());
        dto.setSenderName(training.getSenderName());
        dto.setType(training.getType());
        return dto;
    }

}
