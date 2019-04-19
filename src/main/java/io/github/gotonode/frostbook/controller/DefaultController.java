package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.Auth;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/")
    public String main(Model model) {
        return "index";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }

    //@PreAuthorize
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/id")
    public String redirectProfile(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        } else {
            String path = profileService.getPath(authentication);
            return "redirect:/id/" + path;
        }
    }

    @PostMapping("/search")
    public String searchPost(@RequestParam String query) {
        
        System.out.println("Search query: " + query);

        query = query.trim();

        if (query.isEmpty()) {
            return "redirect:/search";
        } else {
            return "redirect:/search?query=" + query;
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {

        if (query == null) {
            query = "";
        } else {
            query = query.trim();
        }

        List<Profile> profiles;

        if (query.isEmpty()) {
            profiles = profileService.findAll();
        } else {
            profiles = profileService.find(query);
        }

        model.addAttribute("query", query.trim());
        model.addAttribute("profiles", profiles);
        model.addAttribute("count", profiles.size());

        return "search";
    }
}
