package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.DebugService;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private ProfileService profileService;

    @Autowired
    private DebugService debugService;

    @GetMapping("/debug/createProfile")
    public String createProfile(HttpSession httpSession, @RequestParam(required = false) Boolean admin, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "You cannot create random profiles as the debug-features are not enabled.");
            return "error";
        }

        Profile profile = debugService.createProfile(admin);

        System.out.println("Created new profile: " + profile);

        return "redirect:/search";
    }

    @GetMapping("/debug/login/{handle}")
    public String createProfile(HttpSession httpSession, @PathVariable String handle, Model model) {

        if (httpSession.getAttribute("debug") == null) {
            model.addAttribute("message",
                    "You cannot automatically log in to a profile as the debug-features are not enabled.");
            return "error";
        }

        Profile profile = profileService.findByHandle(handle);

        User user = new User(profile.getHandle(), profile.getPassword(), profile.getSimpleGrantedAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user
                , null,
                profile.getSimpleGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // return "redirect:/id/" + profile.getPath();
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

        return "redirect:/";
    }

    @GetMapping("/debug/enable")
    public String enable(HttpSession httpSession, HttpServletRequest httpServletRequest) {

        System.out.println("Enabling Debug-mode for session: " + httpSession.getId());

        httpSession.setAttribute("debug", true);

        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null || referer.trim().isEmpty()) {
            return "redirect:/";
        }

        return "redirect:" + referer;
    }

    @GetMapping("/debug/disable")
    public String disable(HttpSession httpSession, HttpServletRequest httpServletRequest) {

        System.out.println("Disabling Debug-mode for session: " + httpSession.getId());

        httpSession.removeAttribute("debug");

        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null || referer.trim().isEmpty()) {
            return "redirect:/";
        }

        return "redirect:" + referer;
    }
}
