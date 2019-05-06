package io.github.gotonode.frostbook;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class _Generic {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a random new profile with the given password.
     *
     * @param plaintextPassword The plaintext password to be used. Keep it safe for later use.
     * @return The new profile.
     */
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

    /**
     * Creates a random new profile and sets a password for it that cannot be recovered later.
     *
     * @return The new profile.
     */
    public Profile getNewRandomProfile() {
        return getNewRandomProfile(UUID.randomUUID().toString());
    }

    /**
     * Autologin user.
     *
     * @param profile Which profile to log in.
     * @return The user that was logged in.
     */
    public Profile autoLogin(Profile profile) {

        User user = new User(profile.getHandle(), profile.getPassword(), profile.getSimpleGrantedAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user
                , null,
                profile.getSimpleGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return profile;
    }

    /**
     * Autologin user by handle.
     *
     * @param handle Handle of the user to be logged in.
     * @return The user that was logged in; null otherwise.
     */
    public Profile autoLogin(String handle) {

        Profile profile = profileRepository.findByHandle(handle.trim());

        if (profile == null) {
            return null;
        }

        User user = new User(profile.getHandle(), profile.getPassword(), profile.getSimpleGrantedAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user
                , null,
                profile.getSimpleGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return profile;
    }

    /**
     * Creates a random new profile and saves it to the database.
     *
     * @return An object that contains both the profile (with hashed password) and the plaintext password string.
     */
    public ProfileContainer createAndSaveRandomNewProfile() {

        String plaintextPassword = UUID.randomUUID().toString().substring(0, 32);

        Profile profile = getNewRandomProfile(plaintextPassword);

        profileRepository.save(profile);

        return new ProfileContainer(profile, plaintextPassword);
    }

}