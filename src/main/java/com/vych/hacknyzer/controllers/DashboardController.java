package com.vych.hacknyzer.controllers;

import com.vych.hacknyzer.dto.domain.dashboard.Event;
import com.vych.hacknyzer.dto.domain.dashboard.Team;
import com.vych.hacknyzer.dto.repo.dashboard.EventsRepo;
import com.vych.hacknyzer.dto.repo.dashboard.TeamsRepo;
import com.vych.hacknyzer.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class DashboardController {
    @Autowired
    EventsRepo eventsRepo;

    @Autowired
    TeamsRepo teamsRepo;

    //region Routes
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboardIndexRoute(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("isAdmin", UserUtils.getAdminFlag());
        model.addAttribute("user", UserUtils.getUserDetails());
        model.addAttribute("events", eventsRepo.findAll()); // TODO: Заменить на последние N

        return "dashboard/dashboard";
    }

    @RequestMapping(value = "/dashboard/{eventId}", method = RequestMethod.GET)
    public String dashboardEventRoute(@PathVariable Long eventId, Model model) {
        Optional<Event> event = eventsRepo.findById(eventId);

        if (event.isEmpty()) {
            return "redirect:/error?eventNotExist";
        }

        model.addAttribute("event", event.get());
        model.addAttribute("user", UserUtils.getUserDetails());

        return "dashboard/event";
    }

    @RequestMapping(value = "/dashboard/{eventId}/{teamId}", method = RequestMethod.GET)
    public String dashboardTeamRoute(@PathVariable Long eventId, @PathVariable Long teamId, Model model) {
        Optional<Team> teamOp = teamsRepo.findById(teamId);
        if (teamOp.isEmpty()) {
            return "redirect:/error?teamNotExist";
        }
        Team team = teamOp.get();

        String username = UserUtils.getUsername();


        model.addAttribute("isLeader", team.getLeader().equals(username));
        model.addAttribute("isMember", team.getMembers().stream().anyMatch(member -> member.equals(username)));
        model.addAttribute("team", team);
        model.addAttribute("user", UserUtils.getUserDetails());
        model.addAttribute("eventId", team.getEvent().getId());

        return "dashboard/team";
    }
    //endregion

    @RequestMapping(value = "/dashboard/{eventId}/createTeam", method = RequestMethod.POST)
    public String dashboardCreateTeamMethod(
            @PathVariable Long eventId,
            @RequestBody MultiValueMap<String, String> formData
    ) {
        String username = formData.get("username").get(0);
        String title = formData.get("title").get(0);
        String shortDescription = formData.get("shortDescription").get(0);
        String fullDescription = formData.get("fullDescription").get(0);

        Optional<Event> eventOp = eventsRepo.findById(eventId);
        if (eventOp.isEmpty()) {
            return "redirect:/error?eventNotExist";
        }

        Event event = eventOp.get();
        if (event.isRegistrationClosed()) {
            return "redirect:/error?eventRegIsClosed";
        }

        Team team = new Team()
                .setEvent(event)
                .setTitle(title)
                .setShortDescription(shortDescription)
                .setDescription(fullDescription)
                .setLeader(username)
                .setMembers(new ArrayList<>());

        teamsRepo.save(team);

        return "redirect:/dashboard/" + eventId;
    }

    @RequestMapping(value = "/dashboard/{eventId}/closeRegistration", method = RequestMethod.POST)
    public String dashboardCloseRegistrationMethod(
            @PathVariable Long eventId,
            @RequestBody MultiValueMap<String, String> formData
    ) {
        String username = formData.get("username").get(0);

        Optional<Event> eventOp = eventsRepo.findById(eventId);
        if (eventOp.isEmpty()) {
            return "redirect:/error?eventNotExist";
        }

        Event event = eventOp.get();

        event.setRegistrationClosed(true);
        eventsRepo.save(event);

        return "redirect:/dashboard/" + eventId;
    }

    @RequestMapping(value = "/dashboard/{eventId}/finishEvent", method = RequestMethod.POST)
    public String dashboardFinishEventMethod(
            @PathVariable Long eventId,
            @RequestBody MultiValueMap<String, String> formData
    ) {
        String username = formData.get("username").get(0);

        Optional<Event> eventOp = eventsRepo.findById(eventId);
        if (eventOp.isEmpty()) {
            return "redirect:/error?eventNotExist";
        }

        Event event = eventOp.get();
        event.setFinished(true);
        eventsRepo.save(event);

        return "redirect:/dashboard/" + eventId;
    }

    @RequestMapping(value = "/dashboard/{eventId}/{teamId}/joinTeam", method = RequestMethod.POST)
    public String dashboardJoinTeamMethod(
            @PathVariable Long eventId,
            @PathVariable Long teamId,
            @RequestBody MultiValueMap<String, String> formData
    ) {
        String username = formData.get("username").get(0);

        Optional<Team> teamOp = teamsRepo.findById(teamId);
        if (teamOp.isEmpty()) {
            return "redirect:/error?teamNotExist";
        }

        Team team = teamOp.get();
        team.getMembers().add(username);

        teamsRepo.save(team);

        return "redirect:/dashboard/" + eventId + "/" + teamId;
    }

    @RequestMapping(value = "/dashboard/{eventId}/{teamId}/leaveTeam", method = RequestMethod.POST)
    public String dashboardLeaveTeamMethod(
            @PathVariable Long eventId,
            @PathVariable Long teamId,
            @RequestBody MultiValueMap<String, String> formData
    ) {
        String username = formData.get("username").get(0);

        Optional<Team> teamOp = teamsRepo.findById(teamId);
        if (teamOp.isEmpty()) {
            return "redirect:/error?teamNotExist";
        }

        Team team = teamOp.get();
        team.getMembers().remove(username);

        teamsRepo.save(team);

        return "redirect:/dashboard/" + eventId + "/" + teamId;
    }
}
