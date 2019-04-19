package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
}
