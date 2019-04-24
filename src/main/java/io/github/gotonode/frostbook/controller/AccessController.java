package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.*;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.LoginData;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.service.CommentService;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.form.RegisterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AccessController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private CommentService commentService;

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
            model.addAttribute("message", "User is logged in. Cannot register for a new account. Please return to the main page.");
            return "error";
        } else {
            return "register";
        }
    }

    @PostMapping("/register")
    public String registerPost(HttpServletRequest request,
                               @Valid @ModelAttribute RegisterData registerData,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        System.out.println("Registering with: " + registerData);

        Profile profile = profileService.create(registerData);

        profileService.login(profile);

        return "redirect:/id/" + profile.getPath();
    }

    @PostMapping("/id/{path}/addComment")
    public String addComment(Model model, @PathVariable String path,
                             @Valid @ModelAttribute CommentData commentData, Authentication authentication) {

        Comment comment = commentService.add(path, commentData, authentication);

        System.out.println("Posted a new comment: " + comment);

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/addComment/{id}")
    public String addSubcommentToComment(Model model, @PathVariable String path, @PathVariable Long id,
                                         @Valid @ModelAttribute SubcommentData subcommentData, Authentication authentication) {

        Subcomment subcomment = commentService.addSubcommentToComment(path, subcommentData, authentication, id);

        System.out.println("Posted a new subcomment: " + subcomment);

        return "redirect:/id/" + path;
    }

}
