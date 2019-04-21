package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.*;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.LoginData;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.service.CommentService;
import io.github.gotonode.frostbook.service.ImageService;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.form.RegisterData;
import io.github.gotonode.frostbook.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class AccessController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

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

    @GetMapping("/id/{path}")
    public String okay(@PathVariable String path, Model model) {
        System.out.println("Requesting path: " + path);

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message", "No profile was found who owns that path. Please try the search functionality.");
            return "error";
        }

        model.addAttribute("profile", profile);

        return "profile";
    }

    @GetMapping("/id/{path}/gallery")
    public String gallery(@PathVariable String path, Model model) {
        System.out.println("Requesting gallery for path: " + path);

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message", "No profile was found who owns that path. Please try the search functionality.");
            return "error";
        }

        model.addAttribute("profile", profile);

        return "gallery";
    }

    @GetMapping("/id/{path}/gallery/{id}")
    public String image(@PathVariable String path, Model model, @PathVariable Long id) {
        System.out.println("Requesting gallery for path: " + path);

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message", "No profile was found who owns that path. Please try the search functionality.");
            return "error";
        }

        Image image = imageService.findById(id);

        model.addAttribute("profile", profile);
        model.addAttribute("image", image);

        return "image";
    }

    @GetMapping("/requests")
    public String requests(Model model) {

        List<Request> requests = requestService.getRequests();

        model.addAttribute("requests", requests);
        return "requests";
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

    @GetMapping("/friends")
    public String friends(Model model, Authentication authentication) {

        List<Profile> friends = profileService.getFriends(authentication);

        model.addAttribute("friends", friends);
        return "friends";
    }

    @GetMapping(value = "/img/{id}/content", produces = "image/png")
    @ResponseBody
    public byte[] image(@PathVariable long id) throws IOException {
        return imageService.getImageContent(id);
    }

    @PostMapping("/img")
    @ResponseBody
    public Image post(@RequestParam MultipartFile file, Authentication authentication) throws IOException {

        if (file.getContentType() == null) {
            return null;
        }

        return imageService.create(file, authentication);
    }
}
