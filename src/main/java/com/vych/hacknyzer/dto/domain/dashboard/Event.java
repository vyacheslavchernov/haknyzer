package com.vych.hacknyzer.dto.domain.dashboard;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@SuppressWarnings("com.haulmont.jpb.LombokDataInspection")
@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private boolean registrationClosed;
    private boolean finished;
    private String organizer;

    @OneToMany(mappedBy = "event")
    private List<Team> teams;

    public Event setTitle(String title) {
        this.title = title;
        return this;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public Event setRegistrationClosed(boolean registrationClosed) {
        this.registrationClosed = registrationClosed;
        return this;
    }

    public Event setFinished(boolean finished) {
        this.finished = finished;
        return this;
    }

    public Event setOrganizer(String organizer) {
        this.organizer = organizer;
        return this;
    }

    public Event setTeams(List<Team> teams) {
        this.teams = teams;
        return this;
    }
}
