package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/id")
    public String redirectProfile(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        } else {
            String path = profileService.getPath(authentication);
            return "redirect:/id/" + path;
        }
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

    @GetMapping("/friends")
    public String friends(Model model, Authentication authentication) {

        List<Profile> friends = profileService.getFriends(authentication);

        model.addAttribute("friends", friends);
        return "friends";
    }

}
