package io.github.gotonode.frostbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/search")
    public String searchPost(@RequestBody String query) {
        System.out.println("Search query: " + query);

        query = query.replace("query=", "");

        query = query.trim();

        return "redirect:/search?query=" + query;
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {

        model.addAttribute("query", query.trim());

        return "search";

    }
}
