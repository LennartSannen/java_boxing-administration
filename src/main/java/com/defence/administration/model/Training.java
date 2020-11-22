package com.defence.administration.model;

import com.defence.administration.enums.TrainingType;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_id_generator")
    @SequenceGenerator(name="training_id_generator", sequenceName = "training_id_seq", allocationSize=1)
    private Long id;
    private LocalDate date;
    private String trainerName;
    private String senderName;
    @Enumerated(EnumType.STRING)
    private TrainingType type;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Training_Member", 
        joinColumns = { @JoinColumn(name = "training_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "member_id") }
    )
    private Set <Member> members = new HashSet<>();

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
    
    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

}
