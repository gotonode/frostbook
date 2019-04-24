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

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
