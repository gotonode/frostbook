package io.github.gotonode.frostbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class DebugController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/debug/createProfile")
    public String createProfile() {

        String handle = UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 8);
        String path = UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        String name = UUID.randomUUID().toString().substring(0, 8)
                + " "
                + UUID.randomUUID().toString().substring(0, 8);

        Profile profile = profileService.add(handle, password, name, path);

        System.out.println("Created new profile: " + profile);

        return "redirect:/";
    }
}
