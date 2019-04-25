package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Image;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.SubcommentData;
import io.github.gotonode.frostbook.repository.CommentRepository;
import io.github.gotonode.frostbook.repository.ImageRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.SubcommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Comment addComment(CommentData commentData, String handle, String path) {

        Profile fromProfile = profileRepository.findProfileByHandle(handle);

        Comment comment = new Comment();

        comment.setComment(commentData.getComment());
        comment.setDate(Date.from(Instant.now()));
        comment.setFromProfile(fromProfile);

        Profile toProfile = profileRepository.findProfileByPath(path);

        commentRepository.save(comment);

        toProfile.getComments().add(comment);

        profileRepository.save(toProfile);

        return comment;
    }

    @Transactional
    public Subcomment addSubcommentToComment(SubcommentData subcommentData, String handle, Long id) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return null;
        }

        Subcomment subcomment = createSubcommentFromSubcommentData(subcommentData, handle);

        subcommentRepository.save(subcomment);

        comment.getSubcomments().add(subcomment);

        commentRepository.save(comment);

        return subcomment;
    }

    @Transactional
    public Subcomment addSubcommentToImage(SubcommentData subcommentData, String handle, Long id) {

        Image image = imageRepository.findById(id).orElse(null);

        if (image == null) {
            return null;
        }

        Subcomment subcomment = createSubcommentFromSubcommentData(subcommentData, handle);

        subcommentRepository.save(subcomment);

        image.getSubcomments().add(subcomment);

        imageRepository.save(image);

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
}
