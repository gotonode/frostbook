package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Image;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.repository.CommentRepository;
import io.github.gotonode.frostbook.repository.ImageRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.SubcommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Service
public class SubcommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubcommentRepository subcommentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Subcomment addToComment(SubcommentData subcommentData, Authentication authentication, Long id) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return null;
        }

        Subcomment subcomment = createSubcommentFromSubcommentData(subcommentData, authentication.getName());

        subcommentRepository.save(subcomment);

        comment.getSubcomments().add(subcomment);

        commentRepository.save(comment);

        return subcomment;
    }

    @Transactional
    public Subcomment addToImage(SubcommentData subcommentData, Authentication authentication, Long id) {

        Image image = imageRepository.findById(id).orElse(null);

        if (image == null) {
            return null;
        }

        Subcomment subcomment = createSubcommentFromSubcommentData(subcommentData, authentication.getName());

        subcommentRepository.save(subcomment);

        image.getSubcomments().add(subcomment);

        imageRepository.save(image);

        return subcomment;

    }

    @Transactional
    public Subcomment remove(long commentId, long subcommentId, String path, Authentication authentication) {

        Profile myProfile = profileRepository.findProfileByHandle(authentication.getName());

        Comment comment = commentRepository.findById(commentId).orElse(null);
        Subcomment subcomment = subcommentRepository.findById(subcommentId).orElse(null);

        if (comment == null || subcomment == null) {
            return null;
        }

        if (!myProfile.getPath().equals(path)) {
            // This subcomment does not reside on our wall.

            if (!subcomment.getFromProfile().equals(myProfile)) {
                // This subcomment is not made by us.

                return null;
            }
        }

        comment.getSubcomments().remove(subcomment);

        commentRepository.save(comment);

        subcommentRepository.delete(subcomment);

        return subcomment;

    }

    private Subcomment createSubcommentFromSubcommentData(SubcommentData subcommentData, String handle) {

        Profile profile = profileRepository.findProfileByHandle(handle);

        Subcomment subcomment = new Subcomment();

        subcomment.setComment(subcommentData.getComment());
        subcomment.setDate(Date.from(Instant.now()));
        subcomment.setFromProfile(profile);

        return subcomment;
    }

    public Subcomment removeFromImage(long imageId, long subcommentId, String path, Authentication authentication) {

        Profile profile = profileRepository.findProfileByHandle(authentication.getName());

        Image image = imageRepository.findById(imageId).orElse(null);

        Subcomment subcomment = subcommentRepository.findById(subcommentId).orElse(null);

        if (image == null || subcomment == null) {
            return null;
        }

        if (!image.getSubcomments().contains(subcomment)) {
            // This subcomment doesn't belong to this image.
            return null;
        }

        if (!profile.getPath().equals(path)) {
            // This subcomment does not reside on our wall.

            if (!subcomment.getFromProfile().equals(profile)) {
                // This subcomment is not made by us.

                return null;
            }
        }

        image.getSubcomments().remove(subcomment);

        imageRepository.save(image);

        subcommentRepository.delete(subcomment);

        return subcomment;
    }
}
