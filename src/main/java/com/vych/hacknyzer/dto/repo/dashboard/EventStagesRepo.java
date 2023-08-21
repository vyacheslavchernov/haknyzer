package com.vych.hacknyzer.dto.repo.dashboard;

import com.vych.hacknyzer.dto.domain.dashboard.EventStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStagesRepo extends JpaRepository<EventStage, Long> {
}
