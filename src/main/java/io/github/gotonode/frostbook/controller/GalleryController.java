package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Image;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.ImageService;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class GalleryController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ImageService imageService;

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

    @GetMapping(value = "/img/{id}/content", produces = "image/png")
    @ResponseBody
    public byte[] image(@PathVariable long id) {
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
