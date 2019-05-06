package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.repository.CommentRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.SubcommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubcommentRepository subcommentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public Comment addComment(CommentData commentData, Authentication authentication, String path) {

        Profile fromProfile = profileRepository.findByHandle(authentication.getName());

        Comment comment = new Comment();

        comment.setComment(commentData.getComment());
        comment.setDate(Date.from(Instant.now()));
        comment.setFromProfile(fromProfile);

        Profile toProfile = profileRepository.findByPath(path);

        commentRepository.save(comment);

        toProfile.getComments().add(comment);

        profileRepository.save(toProfile);

        return comment;
    }

    @Transactional
    public Comment toggleLike(long id, Authentication authentication) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return null;
        }

        Profile profile = profileRepository.findByHandle(authentication.getName());

        if (comment.getLikedBy().contains(profile)) {
            comment.getLikedBy().remove(profile);
        } else {
            comment.getLikedBy().add(profile);
        }

        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Comment remove(long id, String path, Authentication authentication) {

        Profile targetProfile = profileRepository.findByPath(path);
        Profile myProfile = profileRepository.findByHandle(authentication.getName());

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return null;
        }

        if (!targetProfile.getComments().contains(comment)) {
            return null;
        }

        if (!myProfile.getPath().equals(path)) {
            // This comment does not reside on our wall.

            if (!comment.getFromProfile().equals(myProfile)) {
                // This comment is not made by us.

                return null;
            }
        }

        // Delete all subcomments from the comment before we actually delete the comment itself.
        for (Subcomment subcomment : comment.getSubcomments()) {
            subcommentRepository.delete(subcomment);
        }

        // Remove the comment from the profile's wall.
        targetProfile.getComments().remove(comment);

        profileRepository.save(targetProfile);

        commentRepository.delete(comment);

        return comment;

    }
}
