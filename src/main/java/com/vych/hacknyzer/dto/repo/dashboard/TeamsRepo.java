package com.vych.hacknyzer.dto.repo.dashboard;

import com.vych.hacknyzer.dto.domain.dashboard.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsRepo extends JpaRepository<Team, Long> {
}
