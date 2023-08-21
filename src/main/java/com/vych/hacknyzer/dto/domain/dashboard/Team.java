package com.vych.hacknyzer.dto.domain.dashboard;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@SuppressWarnings("com.haulmont.jpb.LombokDataInspection")
@Entity
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    private String title;
    private String shortDescription;
    private String description;
    private List<String> members;
    private String leader;

    public Team setId(Long id) {
        this.id = id;
        return this;
    }

    public Team setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Team setTitle(String title) {
        this.title = title;
        return this;
    }

    public Team setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public Team setDescription(String description) {
        this.description = description;
        return this;
    }

    public Team setMembers(List<String> members) {
        this.members = members;
        return this;
    }

    public Team setLeader(String leader) {
        this.leader = leader;
        return this;
    }
}
