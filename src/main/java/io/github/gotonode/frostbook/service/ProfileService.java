package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.details.CustomUserDetailsService;
import io.github.gotonode.frostbook.domain.Image;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.form.RegisterData;
import io.github.gotonode.frostbook.repository.ImageRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public Profile add(String handle, String plaintextPassword, String name, String path, List<String> authorities) {

        String password = passwordEncoder.encode(plaintextPassword);

        Profile profile = new Profile(handle, password, name, path);
        profile.setDate(Date.from(Instant.now()));

        profile.setAuthorities(authorities);

        return profileRepository.save(profile);
    }

    /**
     * Sends a friend request to this user.
     *
     * @param fromProfileId The ID the request originates from.
     * @param toProfileId   The ID of the target.
     */
    public void befriend(long fromProfileId, long toProfileId) {
        Profile fromProfile = profileRepository.getOne(fromProfileId);
        Profile toProfile = profileRepository.getOne(toProfileId);

        Request request = new Request();
        request.setDate(Date.from(Instant.now()));
        request.setFromProfile(fromProfile);

        toProfile.getRequests().add(request);
    }

    public List<Profile> find(String query) {
        return profileRepository.findAllByHandleContainingOrNameContainingOrPathContainingOrderByIdAsc(query, query, query);
    }

    public List<Profile> findAll() {
        return profileRepository.findAllByOrderByIdAsc();
    }

    public Profile findByPath(String path) {
        return profileRepository.findByPath(path);
    }

    public Profile create(RegisterData registerData) {

        Profile profile = new Profile();

        String name = registerData.getName().trim();

        String singleSpace = " ";
        String doubleSpace = singleSpace + singleSpace;

        // Remove annoying double spaces from name. Handle and path cannot contain spaces at all.
        while (name.contains(doubleSpace)) {
            name = name.replace(doubleSpace, singleSpace);
        }

        registerData.setName(name);

        profile.setDate(Date.from(Instant.now()));
        profile.setHandle(registerData.getHandle().trim());
        profile.setName(registerData.getName().trim());
        profile.setPassword(passwordEncoder.encode(registerData.getPassword()));
        profile.setPath(registerData.getPath().trim());

        profile.getAuthorities().add("USER");

        return profileRepository.save(profile);
    }

    public List<Profile> getFriends(Authentication authentication) {
        Profile currentProfile = profileRepository.findByHandle(authentication.getName());

        return currentProfile.getFriends();
    }

    /**
     * Automatically logs the specified user in.
     *
     * @param profile The user to log in.
     */
    public void login(Profile profile) {

        User user = new User(profile.getHandle(), profile.getPassword(), profile.getSimpleGrantedAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user
                , null,
                profile.getSimpleGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Profile findByHandle(String handle) {
        return profileRepository.findByHandle(handle);
    }

    public Profile findByAuthentication(Authentication authentication) {
        return profileRepository.findByHandle(authentication.getName());
    }

    public Profile setAsProfileImage(long id, Authentication authentication) {

        Profile profile = profileRepository.findByHandle(authentication.getName());

        Image image = imageRepository.findById(id).orElse(null);

        // We don't own this image, so we cannot set it as our profile image.
        if (!profile.getImages().contains(image)) {
            return null;
        }

        if (image == null) {
            return null;
        }

        profile.setProfileImage(image);

        return profileRepository.save(profile);
    }

    public boolean isFriendsWith(Profile profile, Authentication authentication) {

        Profile myProfile = profileRepository.findByHandle(authentication.getName());

        if (myProfile.equals(profile)) {
            // You're always friends with yourself. :-)
            return true;
        }

        return myProfile.getFriends().contains(profile);
    }

    public boolean isFriendsWith(String path, Authentication authentication) {

        Profile targetProfile = profileRepository.findByPath(path);

        return isFriendsWith(targetProfile, authentication);
    }
}
