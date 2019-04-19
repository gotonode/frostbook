package io.github.gotonode.frostbook;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public abstract class Auth implements Authentication {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile getProfile() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileRepository.findProfileByHandle(name);
    }
}
