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
public class SearchIG extends FluentTest {

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
    public void existingUserCanLogin() {

        String cardHeaderString = "card-header";

        goTo("http://localhost:" + port + "/search");

        assertFalse(pageSource().contains(cardHeaderString));

        for (int i = 0; i < 10; i++) {

            String plaintextPassword = UUID.randomUUID().toString().substring(0, 32);

            Profile profile = _generic.getNewRandomProfile(plaintextPassword);

            profileRepository.save(profile);
        }

        goTo("http://localhost:" + port + "/search");

        // TODO: Count the instances here so that they are an exact match!
        assertTrue(pageSource().contains(cardHeaderString));
    }

}