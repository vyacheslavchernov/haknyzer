package com.vych.hacknyzer.dto.domain.dashboard;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@SuppressWarnings("com.haulmont.jpb.LombokDataInspection")
@Entity
@Data
public class EventStage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime start;
    private String title;
    private String description;
}
