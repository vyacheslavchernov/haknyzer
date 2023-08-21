package com.vych.hacknyzer.controllers;

import com.vych.hacknyzer.dto.domain.dashboard.Event;
import com.vych.hacknyzer.dto.repo.dashboard.EventsRepo;
import com.vych.hacknyzer.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

    @Autowired
    EventsRepo eventsRepo;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPanelRoute(Model model) {
        model.addAttribute("isAdmin", UserUtils.getAdminFlag());
        model.addAttribute("user", UserUtils.getUserDetails());

        return "admin/adminPanel";
    }

    @RequestMapping(value = "/admin/createEvent", method = RequestMethod.POST)
    public String adminPanelRoute(@RequestBody MultiValueMap<String, String> formData) {
        String username = formData.get("username").get(0);
        String title = formData.get("title").get(0);
        String description = formData.get("description").get(0);

        if (eventsRepo.findByTitle(title).isPresent()) {
            return "redirect:/error?titleAlreadyTaken";
        }

        Event event = new Event()
                .setTitle(title)
                .setDescription(description)
                .setFinished(false)
                .setRegistrationClosed(false)
                .setOrganizer(username);

        eventsRepo.save(event);

        return "redirect:/admin";
    }
}
