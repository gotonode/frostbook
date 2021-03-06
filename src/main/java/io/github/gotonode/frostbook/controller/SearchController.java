package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ProfileService profileService;

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

        model.addAttribute("query", query);
        model.addAttribute("profiles", profiles);
        model.addAttribute("count", profiles.size());

        return "search";
    }

    @PostMapping("/search")
    public String searchPost(@RequestParam String query) {

        query = query.trim();

        if (query.isEmpty()) {

            System.out.println("Search (with empty query)");

            return "redirect:/search";
        } else {

            System.out.println("Search query: " + query);

            return "redirect:/search?query=" + query;
        }
    }
}
