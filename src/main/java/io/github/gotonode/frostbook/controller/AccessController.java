package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.form.RegisterData;
import io.github.gotonode.frostbook.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccessController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/login")
    public String login(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("message", "User is already logged in. Please return to the main page.");
            return "error";
        } else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("message", "User is logged in. Cannot register for a new account. Please return to the main page.");
            return "error";
        } else {
            return "register";
        }
    }

    @PostMapping("/register")
    public String registerPost(HttpServletRequest request,
                               @Valid @ModelAttribute RegisterData registerData) throws ServletException {

        System.out.println("Registering with: " + registerData);

        Profile profile = profileService.create(registerData);

        profileService.login(profile);

        return "redirect:/id/" + profile.getPath();
    }

    @GetMapping("/id/{path}")
    public String okay(@PathVariable String path, Model model) {
        System.out.println("Requesting path: " + path);

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message", "No profile was found who owns that path. Please try the search functionality.");
            return "error";
        }

        model.addAttribute("profile", profile);

        return "profile";
    }

    @GetMapping("/requests")
    public String requests(Model model) {

        List<Request> requests = requestService.getRequests();

        model.addAttribute("requests", requests);
        return "requests";
    }

    @PostMapping("/requests/add/{handle}")
    public String addRequest(Model model, @PathVariable String handle, Authentication authentication) {

        List<Profile> friends = profileService.getFriends(authentication);

        Profile targetProfile = profileService.findByHandle(handle);

        List<Request> requests = targetProfile.getRequests();

        // TODO: Optimize and refactor the following!
        for (Request request : requests) {
            if (request.getFromProfile().getHandle().equals(authentication.getName())) {
                model.addAttribute("message",
                        "You've already sent them a friend request. Feeling lonely?");
                return "error";
            }
        }

        // TODO: Optimize and refactor the following!
        for (Profile friend : friends) {
            if (friend.getHandle().equals(handle.trim())) {
                model.addAttribute("message",
                        "Cannot send a friend request to an existing friend! What would be the point?");
                return "error";
            }
        }

        // TODO: Refactor the following!
        if (authentication.getName().equals(handle.trim())) {
            model.addAttribute("message", "As sad as it is, you cannot add yourself as your own friend.");
            return "error";
        }

        requestService.create(handle);

        return "redirect:/id/" + targetProfile.getPath();
    }

    @PostMapping("/requests/{handle}/accept")
    public String confirmRequest(@PathVariable String handle) {

        requestService.accept(handle);

        return "redirect:/requests";
    }

    @PostMapping("/requests/{handle}/remove")
    public String removeRequest(@PathVariable String handle) {

        requestService.remove(handle);

        return "redirect:/requests";
    }

    @GetMapping("/friends")
    public String friends(Model model, Authentication authentication) {

        List<Profile> friends = profileService.getFriends(authentication);

        model.addAttribute("friends", friends);
        return "friends";
    }
}
