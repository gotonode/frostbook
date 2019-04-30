package io.github.gotonode.frostbook.integration;

import io.github.gotonode.frostbook._Generic;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.CommentRepository;
import io.github.gotonode.frostbook.repository.ImageRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.SubcommentRepository;
import io.github.gotonode.frostbook.service.CommentService;
import io.github.gotonode.frostbook.service.GalleryService;
import io.github.gotonode.frostbook.service.ProfileService;
import io.github.gotonode.frostbook.service.RequestService;
import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ComponentScan(value = "io.github.gotonode.frostbook")
public class CommentIG extends FluentTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubcommentRepository subcommentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private _Generic _generic;

    @Test
    public void userCanCommentOnOwnProfile() {

        String randomComment = UUID.randomUUID().toString();

        String plaintextPassword = UUID.randomUUID().toString().substring(0, 32);

        Profile profile = _generic.getNewRandomProfile(plaintextPassword);

        profileRepository.save(profile);

        login(profile, plaintextPassword);

        goTo("http://localhost:" + port + "/id/" + profile.getPath());

        assertTrue(pageSource().contains(profile.getHandle()));

        // Ensure that the profile's wall does not already contain the random comment.
        assertFalse(pageSource().contains(randomComment));

        find("#comment").fill().with(randomComment);

        find("form#post-comment").submit();

        System.out.println(pageSource());

        // Check that the user's wall contains the random comment now.
        assertTrue(pageSource().contains(randomComment));
    }

    private void login(Profile profile, String plaintextPassword) {

        // Load the login page.
        goTo("http://localhost:" + port + "/login");

        // Fill the form with the new user's login data.
        find("#handle").fill().with(profile.getHandle());
        find("#password").fill().with(plaintextPassword);

        // Submit the form, logging in the user and redirecting to index.
        find("form#login").submit();
    }

}