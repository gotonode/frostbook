package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.service.CommentService;
import io.github.gotonode.frostbook.service.SubcommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SubcommentService subcommentService;

    @PostMapping("/id/{path}/comments")
    public String addComment(@PathVariable String path,
                             @Valid @ModelAttribute CommentData commentData, Authentication authentication) {

        System.out.println("Posting a new comment: " + commentData);

        Comment comment = commentService.addComment(commentData, authentication.getName(), path);

        System.out.println("Posted a new comment to the wall: " + comment);

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{id}/like")
    public String like(@PathVariable String path, @PathVariable Long id,
                       Authentication authentication, Model model) {

        Comment comment = commentService.toggleLike(id, authentication.getName());

        if (comment == null) {
            model.addAttribute("message", "This comment has been deleted.");
            return "error";
        }

        System.out.println("Like (toggle) on comment: " + comment);

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{id}/delete")
    public String deleteComment(@PathVariable String path, @PathVariable Long id,
                                Authentication authentication, Model model) {

        Comment comment = commentService.remove(id, path, authentication.getName());

        if (comment == null) {
            model.addAttribute("message", "Cannot delete this comment. Perhaps you're not the owner of it.");
            return "error";
        }

        System.out.println("Deleted comment with ID: " + comment.getId());

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{commentId}/subcomments/{subcommentId}/delete")
    public String deleteSubcomment(@PathVariable String path, @PathVariable Long commentId,
                                   @PathVariable Long subcommentId, Authentication authentication, Model model) {

        Subcomment subcomment = subcommentService.remove(commentId, subcommentId, path, authentication.getName());

        if (subcomment == null) {
            model.addAttribute("message", "Cannot delete this subcomment. Perhaps you're not the owner of it.");
            return "error";
        }

        System.out.println("Deleted subcomment with ID: " + subcomment.getId());

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{id}/subcomments")
    public String addSubcommentToComment(@PathVariable String path, @PathVariable Long id,
                                         @Valid @ModelAttribute SubcommentData subcommentData,
                                         Authentication authentication) {

        System.out.println("Posting a new subcomment (wall): " + subcommentData);

        Subcomment subcomment = subcommentService.addToComment(subcommentData,
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

        Subcomment subcomment = subcommentService.addToImage(subcommentData,
                authentication.getName(), id);

        if (subcomment == null) {
            System.out.println("Could not add comment.");
        } else {
            System.out.println("Added a new subcomment (image): " + subcomment);
        }

        return "redirect:/id/" + path;
    }

}
