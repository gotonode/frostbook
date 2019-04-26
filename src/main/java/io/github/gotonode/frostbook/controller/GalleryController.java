package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.MyApplication;
import io.github.gotonode.frostbook.domain.Image;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.service.ImageService;
import io.github.gotonode.frostbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class GalleryController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/gallery")
    public String base(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        } else {
            String path = profileService.getPath(authentication);
            return "redirect:/id/" + path + "/gallery";
        }
    }

    @GetMapping("/id/{path}/gallery")
    public String gallery(@PathVariable String path, Model model) {

        System.out.println("Requesting gallery for path: " + path);

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message",
                    "No profile was found who owns that gallery path. Please try the search functionality.");
            return "error";
        }

        model.addAttribute("profile", profile);

        return "gallery";
    }

    @GetMapping("/id/{path}/gallery/{id}")
    public String image(@PathVariable String path, Model model, @PathVariable Long id) {

        Profile profile = profileService.findByPath(path);

        if (profile == null) {
            model.addAttribute("message", "No profile was found who owns that gallery image path. Please try the search functionality.");
            return "error";
        }

        Image image = imageService.findById(id);

        model.addAttribute("profile", profile);
        model.addAttribute("image", image);

        return "image";
    }

    @GetMapping("/id/{path}/gallery/{id}/content")
    public ResponseEntity<byte[]> image(@PathVariable Long id) {

        Image image = imageService.findById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.setContentLength(image.getLength());

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.CREATED);
    }

    @PostMapping("/id/{path}/gallery/{id}/like")
    public String like(Model model, @PathVariable String path, @PathVariable Long id,
                       Authentication authentication, HttpServletRequest httpServletRequest) {

        Image image = imageService.like(id, authentication.getName());

        if (image == null) {
            model.addAttribute("message",
                    "Cannot like this image. Either you have already liked it or the image has been deleted.");
            return "error";

        }

        System.out.println("Liked image with ID: " + id);

        String referer = httpServletRequest.getHeader("Referer");

        if (referer == null || referer.trim().isEmpty()) {
            return "redirect:/id/" + path + "/gallery/";
        }

        return "redirect:" + referer;
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam MultipartFile file, @RequestParam String description,
                         Authentication authentication) throws IOException {

        // TODO: Is this method's URI correct for REST?

        // We don't need to pass the path variable as anyone's who's uploading images
        // is only doing so onto their own gallery.

        String type = file.getContentType();

        if (type == null) {
            model.addAttribute("message", "Could not determine image's content type.");
            return "error";
        }

        type = type.trim();

        System.out.println("Attempting to create an image of type: " + type);

        if (!type.equals("image/png") && !type.equals("image/jpeg")) {
            model.addAttribute("message", "Incorrect image type. Please try a JPEG ('JPG') or PNG.");
            return "error";
        }

        Image image = imageService.create(file, description, authentication.getName());

        if (image == null) {
            model.addAttribute("message", "You already have the maximum (" +
                    MyApplication.MAX_GALLERY_IMAGES_PER_USER + ") number of images on your gallery.");
            return "error";
        }

        System.out.println("Created a new image: " + image);

        return "redirect:/gallery";
    }
}
