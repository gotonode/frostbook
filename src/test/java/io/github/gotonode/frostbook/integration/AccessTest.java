package io.github.gotonode.frostbook.integration;

import io.github.gotonode.frostbook._Generic;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.CommentRepository;
import io.github.gotonode.frostbook.repository.ImageRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.SubcommentRepository;
import io.github.gotonode.frostbook.service.CommentService;
import io.github.gotonode.frostbook.service.ImageService;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ComponentScan(value = "io.github.gotonode.frostbook")
public class AccessTest extends FluentTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ImageService imageService;

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
    public void newUserCanRegister() {

        String handle = UUID.randomUUID().toString().substring(0, 8);
        String path = UUID.randomUUID().toString().substring(0, 8);
        String name = UUID.randomUUID().toString().substring(0, 8) + " "
                + UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 32);

        // View all profiles.
        goTo("http://localhost:" + port + "/search");

        // Our profile should not exist.
        assertFalse(pageSource().contains(handle));

        // Start registering process.
        goTo("http://localhost:" + port + "/register");

        // Fill the form with the random data from above.
        find("#handle").fill().with(handle);
        find("#path").fill().with(path);
        find("#name").fill().with(name);
        find("#password").fill().with(password);

        // Submit the form, registering the profile and redirecting to it.
        find("form#register").submit();

        // View all profiles again.
        goTo("http://localhost:" + port + "/search");

        // Our profile should exist now that it was created.
        assertTrue(pageSource().contains(handle));
    }

    @Test
    public void existingUserCanLogin() {

        String plaintextPassword = UUID.randomUUID().toString().substring(0, 32);

        Profile profile = _generic.getNewRandomProfile(plaintextPassword);

        profileRepository.save(profile);

        // View the index.
        goTo("http://localhost:" + port);

        // Our profile should not be logged in.
        assertFalse(pageSource().contains(profile.getHandle()));

        // Load the login page.
        goTo("http://localhost:" + port + "/login");

        // Fill the form with the new user's login data.
        find("#handle").fill().with(profile.getHandle());
        find("#password").fill().with(plaintextPassword);

        // Submit the form, logging in the user and redirecting to index.
        find("form#login").submit();

        // Check that the user was logged in.
        assertTrue(pageSource().contains(profile.getHandle()));
    }

}