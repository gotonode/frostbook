package io.github.gotonode.frostbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class AccessController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestBody String username, @RequestBody String password) {

        return "redirect:/";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestBody Map<String, String> body) {
        System.out.println(body);
        return "redirect:/";
    }

    @GetMapping("/id/{path}")
    public String okay(@PathVariable String path, Model model) {
        System.out.println("Requesting path: " + path);

        User user = userService.findByPath(path);

        if (user == null) {
            model.addAttribute("message", "No user was found who owns that path. Please try the search functionality.");
            return "error";
        }

        model.addAttribute("user", user);

        return "user";
    }
}
