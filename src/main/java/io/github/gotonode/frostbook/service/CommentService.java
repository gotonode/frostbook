package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Subcomment;
import io.github.gotonode.frostbook.form.CommentData;
import io.github.gotonode.frostbook.form.SubcommentData;
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
    public Comment add(String path, CommentData commentData, Authentication authentication) {
        Comment comment = new Comment();

        comment.setComment(commentData.getComment());
        comment.setDate(Date.from(Instant.now()));
        comment.setFromProfile(profileRepository.findProfileByHandle(authentication.getName()));

        Profile toProfile = profileRepository.findProfileByPath(path);

        commentRepository.save(comment);

        toProfile.getComments().add(comment);
        profileRepository.save(toProfile);

        return comment;
    }

    @Transactional
    public Subcomment addSubcommentToComment(String path, SubcommentData subcommentData, Authentication authentication, Long id) {
        Comment comment = commentRepository.findById(id).get();

        Subcomment subcomment = new Subcomment();

        subcomment.setComment(subcommentData.getComment());
        subcomment.setDate(Date.from(Instant.now()));
        subcomment.setFromProfile(profileRepository.findProfileByHandle(authentication.getName()));

        subcommentRepository.save(subcomment);

        comment.getSubcomments().add(subcomment);

        commentRepository.save(comment);

        return subcomment;
    }
}
