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
    private UserService userService;

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
    public String search(@RequestParam String query, Model model) {

        query = query.trim();

        List<User> users;

        if (query.isEmpty()) {
            users = userService.findAll();
        } else {
            users = userService.find(query);
        }

        model.addAttribute("query", query.trim());
        model.addAttribute("users", users);
        model.addAttribute("count", users.size());

        return "search";

    }

    @GetMapping("/addUser")
    public String addUser(@RequestParam String handle,
                          @RequestParam String name,
                          @RequestParam String password,
                          @RequestParam String path) {

        User user = userService.add(handle, password, name, path);

        System.out.println("Created new user: " + user);

        return "redirect:/";
    }
}
