package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private CustomUserDetailsService userDetails;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RequestRepository requestRepository;

    public List<Request> getRequests() {
        return userDetails.getProfile().getRequests();
    }

    @Transactional
    public void create(String handle) {

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Profile currentProfile = profileRepository.findProfileByHandle(userDetails.getUsername());

        Request request = new Request();
        request.setDate(Date.from(Instant.now()));
        Profile profile = profileRepository.findProfileByHandle(handle);
        request.setFromProfile(currentProfile);

        requestRepository.save(request);

        profile.getRequests().add(request);

        profileRepository.save(profile);
    }

    @Transactional
    public void accept(String handle) {

        Profile fromProfile = profileRepository.findProfileByHandle(handle);

        Request request = requestRepository.findByFromProfile(fromProfile);

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile currentProfile = profileRepository.findProfileByHandle(userDetails.getUsername());

        currentProfile.getFriends().add(fromProfile);
        fromProfile.getFriends().add(currentProfile);

        currentProfile.getRequests().remove(request);
        //fromProfile.getRequests().remove(request);

        profileRepository.save(currentProfile);
        profileRepository.save(fromProfile);

        requestRepository.delete(request);

        // We delete this if it's found. This is the case where two users have
        // each sent a friend request to each other.
        Request secondRequest = requestRepository.findByFromProfile(currentProfile);
        if (secondRequest != null) {
            requestRepository.delete(secondRequest);
        }
    }

    public void remove(String handle) {

        Profile fromProfile = profileRepository.findProfileByHandle(handle);

        Request request = requestRepository.findByFromProfile(fromProfile);

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile currentProfile = profileRepository.findProfileByHandle(userDetails.getUsername());

        currentProfile.getRequests().remove(request);

        profileRepository.save(currentProfile);

        requestRepository.delete(request);
    }
}