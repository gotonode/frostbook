package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/id/{path}/comments")
    public String addComment(@PathVariable String path,
                             @Valid @ModelAttribute CommentData commentData, Authentication authentication) {

        System.out.println("Posting a new comment: " + commentData);

        Comment comment = commentService.addComment(commentData, authentication.getName(), path);

        System.out.println("Posted a new comment to the wall: " + comment);

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{id}/subcomments")
    public String addSubcommentToComment(@PathVariable String path, @PathVariable Long id,
                                         @Valid @ModelAttribute SubcommentData subcommentData,
                                         Authentication authentication) {

        System.out.println("Posting a new subcomment (wall): " + subcommentData);

        Subcomment subcomment = commentService.addSubcommentToComment(subcommentData,
                authentication.getName(), id);

        if (subcomment == null) {
            System.out.println("Could not add comment.");
        } else {
            System.out.println("Added a new subcomment (wall): " + subcomment);
        }

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/gallery/{id}/subcomments")
    public String addSubcommentToImage(@PathVariable String path, @PathVariable Long id,
                                       @Valid @ModelAttribute SubcommentData subcommentData,
                                       Authentication authentication) {

        System.out.println("Posting a new subcomment (image): " + subcommentData);

        Subcomment subcomment = commentService.addSubcommentToImage(subcommentData,
                authentication.getName(), id);

        if (subcomment == null) {
            System.out.println("Could not add comment.");
        } else {
            System.out.println("Added a new subcomment (image): " + subcomment);
        }

        return "redirect:/id/" + path;
    }

}
