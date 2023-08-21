package com.vych.hacknyzer.dto.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@SuppressWarnings("com.haulmont.jpb.LombokDataInspection")
@Entity
@Data
public class UserData {
    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String avatar;

    private String skills;

    public UserData setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserData setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserData setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserData setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public UserData setSkills(String skills) {
        this.skills = skills;
        return this;
    }
}
