package com.vych.hacknyzer.controllers;


import com.vych.hacknyzer.dto.domain.users.UserData;
import com.vych.hacknyzer.dto.repo.users.UserDataRepo;
import com.vych.hacknyzer.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class UsersController {

    @Autowired
    UserDetailsManager userDetailsManager;
    @Autowired
    UserDataRepo userDataRepo;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    //region Routes
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupRoute() {
        return "users/signup";
    }

    @RequestMapping(value = "/user/profile/{username}", method = RequestMethod.GET)
    public String userSpaceRoute(@PathVariable String username, Model model) {
        User user;

        try {
            user = (User) userDetailsManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return "redirect:/error?userNotFound";
        }

        Optional<UserData> userData = userDataRepo.findById(username);
        if (userData.isEmpty()) {
            return "redirect:/error?emptyUserData";
        }

        model.addAttribute("user", user);
        model.addAttribute("userData", userData.get());
        model.addAttribute("selfPage", UserUtils.getUsername().equals(username));

        return "users/userSpace";
    }

    //endregion

    @RequestMapping(value = "/proceed-signup", method = RequestMethod.POST)
    public String signupMethod(@RequestBody MultiValueMap<String, String> formData) {
        String username = formData.get("username").get(0);
        String psw = formData.get("password").get(0);
        String fistName = formData.get("firstName").get(0);
        String lastName = formData.get("lastName").get(0);
        String skills = formData.get("skills").get(0);

        if (username.equals("") || psw.equals("") || fistName.equals("") || lastName.equals("")) {
            return "redirect:/signup?error=input";
        }

        try {
            userDetailsManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            UserDetails user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(psw))
                    .roles("USER")
                    .build();

            UserData userData = new UserData()
                    .setUsername(username)
                    .setFirstName(fistName)
                    .setLastName(lastName)
                    .setAvatar(null)
                    .setSkills(skills);

            userDataRepo.save(userData);
            userDetailsManager.createUser(user);
            return "redirect:/login"; //TODO: Добавить тригер того, что была произведена свежая рега
        }
        return "redirect:/signup?error=exist";
    }

    @RequestMapping(value = "/user/edit/data", method = RequestMethod.POST)
    public String editUserDataMethod(@RequestBody MultiValueMap<String, String> formData) {
        String username = formData.get("username").get(0);
        String fistName = formData.get("firstName").get(0);
        String lastName = formData.get("lastName").get(0);
        String skills = formData.get("skills").get(0);

        UserData userData = new UserData()
                .setUsername(username)
                .setFirstName(fistName)
                .setLastName(lastName)
                .setSkills(skills)
                .setAvatar(null);

        userDataRepo.save(userData);

        return "redirect:/user/profile/" + username + "?success";
    }

    @RequestMapping(value = "/user/edit/psw", method = RequestMethod.POST)
    public String editUserPassword(@RequestBody MultiValueMap<String, String> formData) {
        String username = formData.get("username").get(0);
        String oldPsw = formData.get("oldPsw").get(0);
        String newPsw = formData.get("newPsw").get(0);
        String newPsw2 = formData.get("newPsw2").get(0);

        if (!newPsw.equals(newPsw2)) {
            return "redirect:/error?newPswNotMatch";
        }

        try {
            User oldUser = (User) userDetailsManager.loadUserByUsername(username);
            if (!passwordEncoder.matches(oldPsw, oldUser.getPassword())) {
                return "redirect:/error?oldPswNotMatch";
            }

            UserDetails user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(newPsw))
                    .authorities(oldUser.getAuthorities())
                    .build();

            userDetailsManager.updateUser(user);
        } catch (Exception e) {
            return "redirect:/error?userUpdateError";
        }

        return "redirect:/user/profile/" + username + "?success";
    }
}
