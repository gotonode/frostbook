package io.github.gotonode.frostbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @PostMapping("/search")
    public String searchPost(@RequestBody String query) {
        System.out.println("Search query: " + query);

        query = query.replace("query=", "");

        query = query.trim();

        return "redirect:/search?query=" + query;
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
