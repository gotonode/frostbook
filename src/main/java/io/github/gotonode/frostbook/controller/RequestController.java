package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.service.RequestService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/requests")
    public String requests(Model model) {

        List<Request> requests = requestService.getRequests();

        model.addAttribute("requests", requests);
        return "requests";
    }

    @PostMapping("/requests/add/{handle}")
    public String addRequest(Model model, @PathVariable String handle, Authentication authentication) {

        // TODO: Refactor the following!
        if (authentication.getName().equals(handle.trim())) {
            model.addAttribute("message", "As sad as it is, you cannot add yourself as your own friend.");
            return "error";
        }

        Profile targetProfile = profileService.findByHandle(handle);

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
            if (friend.getHandle().equals(handle.trim())) {
                model.addAttribute("message",
                        "Cannot send a friend request to an existing friend! What would be the point?");
                return "error";
            }
        }

        requestService.create(handle);

        return "redirect:/id/" + targetProfile.getPath();
    }

    @PostMapping("/requests/{handle}/accept")
    public String confirmRequest(@PathVariable String handle) {

        requestService.accept(handle);

        return "redirect:/requests";
    }

    @PostMapping("/requests/{handle}/remove")
    public String removeRequest(@PathVariable String handle) {

        requestService.remove(handle);

        return "redirect:/requests";
    }

    @GetMapping(value = "/query/requests/count", produces = "application/json")
    @ResponseBody
    public String getRequestCount(Authentication authentication) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", requestService.getRequestCount(authentication));
        return jsonObject.toString();
    }
}
