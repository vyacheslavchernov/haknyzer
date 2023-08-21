package com.vych.hacknyzer.dto.repo.dashboard;

import com.vych.hacknyzer.dto.domain.dashboard.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventsRepo extends JpaRepository<Event, Long> {
    Optional<Event> findByTitle(String title);
}
