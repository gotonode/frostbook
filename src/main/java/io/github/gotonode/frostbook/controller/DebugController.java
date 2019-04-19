package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.DebugService;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.UUID;

@Controller
public class DebugController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private DebugService debugService;

    @GetMapping("/debug/createProfile")
    public String createProfile() {

        Profile profile = debugService.createProfile();

        System.out.println("Created new profile: " + profile);

        return "redirect:/search";
    }

    @GetMapping("/debug/login/{handle}")
    public String createProfile(@PathVariable String handle) {

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
    public String reset() {
        debugService.reset();
        return "redirect:/";
    }
}
