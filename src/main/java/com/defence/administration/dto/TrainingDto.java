package com.defence.administration.dto;

import com.defence.administration.enums.TrainingType;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TrainingDto {
    
    private Long id;
    @NotNull
    private LocalDate date;
    @NotBlank
    private String trainerName;
    @NotBlank
    private String senderName;
    private TrainingType type;
    @NotNull
    private Set <MemberDto> members = new HashSet<>();
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public TrainingType getType() {
        return type;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public Set<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(Set<MemberDto> members) {
        this.members = members;
    }

}
