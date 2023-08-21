package com.vych.hacknyzer.dto.repo.users;

import com.vych.hacknyzer.dto.domain.users.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepo extends JpaRepository<UserData, String> {

}
