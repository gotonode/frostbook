package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FriendController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/friends")
    private String base(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        } else {
            Profile profile = profileService.findByHandle(authentication.getName());
            return "redirect:/id/" + profile.getPath() + "/friends";
        }
    }

    @GetMapping("/id/{path}/friends")
    public String friends(Model model, Authentication authentication) {

        List<Profile> friends = profileService.getFriends(authentication);

        model.addAttribute("friends", friends);
        return "friends";
    }

    @PostMapping("/id/{path}/friends/add")
    public String add(Model model, @PathVariable String path, Authentication authentication) {

        Profile targetProfile = profileService.findByPath(path);

        // TODO: Refactor the following!
        if (authentication.getName().equals(targetProfile.getHandle())) {
            model.addAttribute("message", "As sad as it is, you cannot add yourself as your own friend.");
            return "error";
        }

        List<Request> requests = targetProfile.getRequests();

        // TODO: Optimize and refactor the following!
        for (Request request : requests) {
            if (request.getFromProfile().getHandle().equals(authentication.getName())) {
                model.addAttribute("message",
                        "You've already sent them a friend request. Feeling lonely?");
                return "error";
            }
        }

        List<Profile> friends = profileService.getFriends(authentication);

        // TODO: Optimize and refactor the following!
        for (Profile friend : friends) {
            if (friend.getHandle().equals(targetProfile.getHandle())) {
                model.addAttribute("message",
                        "Cannot send a friend request to an existing friend! What would be the point?");
                return "error";
            }
        }

        requestService.befriend(targetProfile, authentication.getName());

        return "redirect:/id/" + targetProfile.getPath();
    }

    @PostMapping("/id/{path}/friends/remove")
    public String remove(Model model, @PathVariable String path, Authentication authentication) {

        Profile targetProfile = profileService.findByPath(path);

        // TODO: Refactor the following!
        if (authentication.getName().equals(targetProfile.getHandle())) {
            model.addAttribute("message", "Cannot unfriend yourself. How sad.");
            return "error";
        }

        requestService.unfriend(targetProfile, authentication.getName());

        return "redirect:/friends";
    }
}
