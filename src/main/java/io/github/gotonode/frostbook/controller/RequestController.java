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
    private String base(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        } else {
            Profile profile = profileService.findByAuthentication(authentication);
            return "redirect:/id/" + profile.getPath() + "/requests";
        }
    }

    @GetMapping("/id/{path}/requests")
    public String requests(Model model) {

        List<Request> requests = requestService.getRequests();

        List<Request> sentRequests = requestService.getSentRequests();

        model.addAttribute("requests", requests);
        model.addAttribute("sentRequests", sentRequests);
        return "requests";
    }

    @PostMapping("/id/{path}/requests/accept")
    public String accept(@PathVariable String path) {

        requestService.accept(path);

        return "redirect:/requests";
    }

    @PostMapping("/id/{path}/requests/remove")
    public String remove(@PathVariable String path) {

        requestService.remove(path);

        return "redirect:/requests";
    }

    @GetMapping(value = "/query/requests/count", produces = "application/json")
    @ResponseBody
    public String count(Authentication authentication) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", requestService.getRequestCount(authentication));
        return jsonObject.toString();
    }
}
