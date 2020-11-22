package com.defence.administration.controller;

import com.defence.administration.dto.TrainingDto;
import com.defence.administration.enums.TrainingType;
import com.defence.administration.model.Member;
import com.defence.administration.model.Training;
import com.defence.administration.service.TrainingService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainingController {

    @Autowired
    private TrainingService service;
    
    @CrossOrigin(origins = "*")
    @GetMapping("/trainings")
    public ResponseEntity getAllTraining() {
        return ResponseEntity.ok(service.findAll().stream().map(service::map).collect(Collectors.toList()));
    }

    @CrossOrigin(origins = "*")    
    @GetMapping("/trainings/{id}")
    public ResponseEntity getTraining(@PathVariable Long id) {
        Optional<Training> training = service.findById(id);
        if (training.isPresent()) {
            return ResponseEntity.ok(service.map(training.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/member/{id}/trainings")
    public ResponseEntity getTrainingsOfMember(@PathVariable Long id) {
        Optional<Member> member = service.findMemberById(id);
        if (!member.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findTrainingsOfMember(member.get()));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/trainings")
    public ResponseEntity postTraining(@RequestBody @Valid TrainingDto dto) throws URISyntaxException {
        if (dto.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.created(new URI(String.valueOf(service.saveAndFlush(service.map(dto)).getId()))).build();
    }

    @CrossOrigin(origins = "*")    
    @PutMapping("/trainings/{id}")
    public ResponseEntity putTraining(@RequestBody @Valid TrainingDto dto, @PathVariable Long id) {
        Optional<Training> training = service.findById(id);
        if (!training.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.update(training.get(), dto);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*")    
    @DeleteMapping("/trainings/{id}")
    public ResponseEntity deleteTraining(@PathVariable Long id) {
        Optional<Training> training = service.findById(id);
        if (!training.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(training.get());
        return ResponseEntity.noContent().build();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/trainings/types")
    public ResponseEntity getTrainingTypes() {
        return ResponseEntity.ok(new ArrayList<>(EnumSet.allOf(TrainingType.class)));
    }

}