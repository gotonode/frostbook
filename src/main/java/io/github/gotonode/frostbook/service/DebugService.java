package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Comment;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DebugService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubcommentRepository subcommentRepository;

    @Autowired
    private ProfileService profileService;

    @Transactional
    public void reset() {

        SecurityContextHolder.clearContext();

        subcommentRepository.deleteAll();
        commentRepository.deleteAll();
        requestRepository.deleteAll();
        imageRepository.deleteAll();
        profileRepository.deleteAll();
    }

    public Profile createProfile(Boolean admin) {

        List<String> authorities = new ArrayList<>();

        authorities.add("DEBUG");

        if (admin != null && admin) {
            authorities.add("ADMIN");
        }

        String handle = UUID.randomUUID().toString().substring(0, 8);
        String password = "debug";
        String path = UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        String name = UUID.randomUUID().toString().substring(0, 8)
                + ' '
                + UUID.randomUUID().toString().substring(0, 8);

        return profileService.add(handle, password, name, path, authorities);
    }

    public void login(String handle) {

        Profile profile = profileService.findByHandle(handle.trim());

        User user = new User(profile.getHandle(), profile.getPassword(), profile.getSimpleGrantedAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user
                , null,
                profile.getSimpleGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    public void populate() {

        Profile profileAdmin = createProfile(true);
        Profile profileUserOne = createProfile(false);
        Profile profileUserTwo = createProfile(false);
        Profile profileUserThree = createProfile(false);
        Profile profileUserFour = createProfile(false);

        profileAdmin.getFriends().add(profileUserOne);
        profileUserOne.getFriends().add(profileAdmin);

        profileAdmin.getFriends().add(profileUserTwo);
        profileUserTwo.getFriends().add(profileAdmin);

        profileRepository.save(profileAdmin);
        profileRepository.save(profileUserOne);
        profileRepository.save(profileUserTwo);
        profileRepository.save(profileUserThree);
        profileRepository.save(profileUserFour);

        Request requestFromUserThree = new Request();
        requestFromUserThree.setDate(Date.from(Instant.now()));
        requestFromUserThree.setFromProfile(profileUserThree);

        Request requestFromUserFour = new Request();
        requestFromUserFour.setDate(Date.from(Instant.now()));
        requestFromUserFour.setFromProfile(profileUserFour);

        requestRepository.save(requestFromUserThree);
        requestRepository.save(requestFromUserFour);

        profileAdmin.getRequests().add(requestFromUserThree);
        profileAdmin.getRequests().add(requestFromUserFour);

        profileRepository.save(profileAdmin);

        Comment commentFromUserOne = new Comment();
        commentFromUserOne.setFromProfile(profileUserOne);
        commentFromUserOne.setDate(Date.from(Instant.now()));
        commentFromUserOne.setComment("Can you help me with something?");

        Comment commentFromUserTwo = new Comment();
        commentFromUserTwo.setFromProfile(profileUserFour);
        commentFromUserTwo.setDate(Date.from(Instant.now()));
        commentFromUserTwo.setComment("Frostbook rules!");

        commentRepository.save(commentFromUserOne);
        commentRepository.save(commentFromUserTwo);

        profileAdmin.getComments().add(commentFromUserOne);
        profileAdmin.getComments().add(commentFromUserTwo);

        profileRepository.save(profileAdmin);
    }

    @Transactional
    public void makePopular(Authentication authentication) {

        Profile profile = profileService.findByHandle(authentication.getName());

        for (int i = 0; i < 5; i++) {

            Profile newProfile = createProfile(false);

            profileRepository.save(newProfile);

            Request request = new Request();
            request.setDate(Date.from(Instant.now()));
            request.setFromProfile(newProfile);

            requestRepository.save(request);

            profile.getRequests().add(request);

            profileRepository.save(profile);
        }

        for (int i = 0; i < 5; i++) {

            Profile newProfile = createProfile(false);

            profileRepository.save(newProfile);

            profile.getFriends().add(newProfile);

            profileRepository.save(profile);
        }

    }
}
