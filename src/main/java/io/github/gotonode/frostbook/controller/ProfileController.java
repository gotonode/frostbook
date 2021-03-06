package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/id")
    public String base(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        } else {
            Profile profile = profileService.findByAuthentication(authentication);
            return "redirect:/id/" + profile.getPath();
        }
    }

    @GetMapping("/id/{path}")
    public String profile(@PathVariable String path, Model model) {

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message", "No profile was found who owns that path. Please try the search functionality.");
            return "error";
        }

        model.addAttribute("profile", profile);

        return "profile";
    }
}
