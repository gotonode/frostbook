package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.form.LoginData;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.form.RegisterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AccessController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/login")
    public String login(Authentication authentication, Model model, @ModelAttribute LoginData loginData) {

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("message", "User is already logged in. Please return to the main page.");
            return "error";
        } else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(Authentication authentication, Model model, @ModelAttribute RegisterData registerData) {

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("message",
                    "User is logged in. Cannot register for a new account. Please return to the main page.");
            return "error";
        } else {
            return "register";
        }
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute RegisterData registerData,
                               BindingResult bindingResult) {

        System.out.println("Registering with: " + registerData);

        if (bindingResult.hasErrors()) {
            return "register";
        }

        Profile profile = profileService.create(registerData);

        profileService.login(profile);

        System.out.println("New profile created and logged in: " + profile);

        return "redirect:/id/" + profile.getPath();
    }

}
