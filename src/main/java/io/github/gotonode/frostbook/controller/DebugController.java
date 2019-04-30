package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class DebugController {

    @Autowired
    private DebugService debugService;

    @GetMapping("/debug")
    public String debug() {
        return "debug";
    }

    @GetMapping("/debug/populate")
    public String populate(HttpSession httpSession, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "Cannot populate the server since debug-mode is off.");
            return "error";
        }

        debugService.populate();

        return "redirect:/search";
    }

    @GetMapping("/debug/makePopular")
    public String makePopular(Authentication authentication, HttpSession httpSession, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "Cannot make you popular since debug-mode is off.");
            return "error";
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("message",
                    "You must be logged in to be made popular.");
            return "error";
        }

        debugService.makePopular(authentication);

        return "redirect:/friends";
    }

    @GetMapping("/debug/createProfile")
    public String createProfile(HttpSession httpSession, @RequestParam(required = false) Boolean admin, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "You cannot create random profiles as the debug-features are not enabled.");
            return "error";
        }

        Profile profile = debugService.createProfile(admin);

        System.out.println("Created new random profile: " + profile);

        return "redirect:/search";
    }

    @GetMapping("/debug/login/{handle}")
    public String autoLogin(HttpSession httpSession, @PathVariable String handle, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "You cannot automatically log in to a profile as the debug-features are not enabled.");
            return "error";
        }

        handle = handle.trim();

        debugService.login(handle);

        System.out.println("Automatically logged in user: " + handle);

        return "redirect:/search";
    }

    @GetMapping("/debug/reset")
    public String reset(HttpSession httpSession, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "You cannot reset the database as the debug-features are not enabled.");
            return "error";
        }

        debugService.reset();

        System.out.println("The database was reset successfully.");

        return "redirect:/";
    }

    @GetMapping("/debug/enable")
    public String enable(HttpSession httpSession, HttpServletRequest httpServletRequest) {

        httpSession.setAttribute("debug", true);

        System.out.println("Enabled Debug-mode for session: " + httpSession.getId());

        String referer = httpServletRequest.getHeader("Referer");

        if (referer == null || referer.trim().isEmpty()) {
            return "redirect:/";
        }

        return "redirect:" + referer;
    }

    @GetMapping("/debug/disable")
    public String disable(HttpSession httpSession, HttpServletRequest httpServletRequest) {

        httpSession.removeAttribute("debug");

        System.out.println("Disabled Debug-mode for session: " + httpSession.getId());

        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null || referer.trim().isEmpty()) {
            return "redirect:/";
        }

        return "redirect:" + referer;
    }
}
