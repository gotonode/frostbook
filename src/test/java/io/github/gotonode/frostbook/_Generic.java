package io.github.gotonode.frostbook;

import io.github.gotonode.frostbook.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class _Generic  {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Profile getNewRandomProfile(String plaintextPassword) {

        Profile profile = new Profile();

        profile.setAuthorities(Collections.singletonList("USER"));
        profile.setDate(Date.from(Instant.now()));
        profile.setHandle(UUID.randomUUID().toString().substring(0, 8));
        profile.setPath(UUID.randomUUID().toString().substring(0, 8));
        profile.setName(UUID.randomUUID().toString().substring(0, 8) + ' ' +
                UUID.randomUUID().toString().substring(0, 8));
        profile.setPassword(passwordEncoder.encode(plaintextPassword));

        return profile;
    }

}