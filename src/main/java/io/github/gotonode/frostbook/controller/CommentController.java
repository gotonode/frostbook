package io.github.gotonode.frostbook.controller;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.service.CommentService;
import io.github.gotonode.frostbook.service.ProfileService;
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
    private ProfileService profileService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SubcommentService subcommentService;

    @PostMapping("/id/{path}/comments")
    public String addComment(@PathVariable String path,
                             @Valid @ModelAttribute CommentData commentData, Authentication authentication,
                             Model model) {

        System.out.println("Posting a new comment: " + commentData);

        if (!profileService.isFriendsWith(path, authentication)) {
            model.addAttribute("message", "You're not friends with them and cannot post a comment.");
            return "error";
        }

        Comment comment = commentService.addComment(commentData, authentication, path);

        System.out.println("Posted a new comment to the wall: " + comment);

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{id}/like")
    public String like(@PathVariable String path, @PathVariable Long id,
                       Authentication authentication, Model model) {

        if (!profileService.isFriendsWith(path, authentication)) {
            model.addAttribute("message", "You're not friends with them and cannot like a comment.");
            return "error";
        }

        Comment comment = commentService.toggleLike(id, authentication);

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

        Comment comment = commentService.remove(id, path, authentication);

        if (comment == null) {
            model.addAttribute("message", "Cannot delete this comment. Perhaps you're not the owner of it.");
            return "error";
        }

        System.out.println("Deleted comment with ID: " + comment.getId());

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/comments/{commentId}/subcomments/{subcommentId}/delete")
    public String deleteSubcommentFromComment(@PathVariable String path, @PathVariable Long commentId,
                                   @PathVariable Long subcommentId, Authentication authentication, Model model) {

        Subcomment subcomment = subcommentService.remove(commentId, subcommentId, path, authentication);

        if (subcomment == null) {
            model.addAttribute("message", "Cannot delete this subcomment. Perhaps you're not the owner of it.");
            return "error";
        }

        System.out.println("Deleted subcomment with ID: " + subcomment.getId());

        return "redirect:/id/" + path;
    }

    @PostMapping("/id/{path}/gallery/{imageId}/subcomments/{subcommentId}/delete")
    public String deleteSubcommentFromImage(@PathVariable String path, @PathVariable Long imageId,
                                   @PathVariable Long subcommentId, Authentication authentication, Model model) {

        Subcomment subcomment = subcommentService.removeFromImage(imageId, subcommentId, path, authentication);

        if (subcomment == null) {
            model.addAttribute("message", "Cannot delete this subcomment. Perhaps you're not the owner of it.");
            return "error";
        }

        System.out.println("Deleted subcomment with ID: " + subcomment.getId());

        return "redirect:/id/" + path + "/gallery/" + imageId;
    }

    @PostMapping("/id/{path}/comments/{id}/subcomments")
    public String addSubcommentToComment(@PathVariable String path, @PathVariable Long id,
                                         @Valid @ModelAttribute SubcommentData subcommentData,
                                         Authentication authentication, Model model) {

        System.out.println("Posting a new subcomment (wall): " + subcommentData);

        if (!profileService.isFriendsWith(path, authentication)) {
            model.addAttribute("message", "You're not friends with them and cannot post a subcomment.");
            return "error";
        }

        Subcomment subcomment = subcommentService.addToComment(subcommentData,
                authentication, id);

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
                                       Authentication authentication, Model model) {

        System.out.println("Posting a new subcomment (image): " + subcommentData);

        if (!profileService.isFriendsWith(path, authentication)) {
            model.addAttribute("message", "You're not friends with them and cannot post a subcomment.");
            return "error";
        }

        Subcomment subcomment = subcommentService.addToImage(subcommentData,
                authentication, id);

        if (subcomment == null) {
            System.out.println("Could not add comment.");
        } else {
            System.out.println("Added a new subcomment (image): " + subcomment);
        }

        return "redirect:/id/" + path + "/gallery/" + id;
    }

}
