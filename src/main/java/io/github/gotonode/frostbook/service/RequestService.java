package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.details.CustomUserDetailsService;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import io.github.gotonode.frostbook.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public void befriend(Profile targetProfile, Authentication authentication) {

        Profile currentProfile = profileRepository.findProfileByHandle(authentication.getName());

        Request request = new Request();
        request.setDate(Date.from(Instant.now()));
        request.setFromProfile(currentProfile);

        requestRepository.save(request);

        targetProfile.getRequests().add(request);

        profileRepository.save(targetProfile);
    }

    @Transactional
    public void unfriend(Profile targetProfile, Authentication authentication) {

        Profile currentProfile = profileRepository.findProfileByHandle(authentication.getName());

        targetProfile.getFriends().remove(currentProfile);
        currentProfile.getFriends().remove(targetProfile);

        profileRepository.save(targetProfile);
        profileRepository.save(currentProfile);
    }

    @Transactional
    public void accept(String path) {

        Profile fromProfile = profileRepository.findProfileByPath(path);

        Request request = requestRepository.findByFromProfile(fromProfile);

        Profile currentProfile = userDetails.getProfile();

        currentProfile.getFriends().add(fromProfile);
        fromProfile.getFriends().add(currentProfile);

        currentProfile.getRequests().remove(request);

        // We delete this if it's found. This is the case where two users have
        // each sent a friend request to each other.
        Request secondRequest = requestRepository.findByFromProfile(currentProfile);
        if (secondRequest != null) {
            fromProfile.getRequests().remove(secondRequest);
            requestRepository.delete(secondRequest);
        }

        profileRepository.save(currentProfile);
        profileRepository.save(fromProfile);

        requestRepository.delete(request);

    }

    public void remove(String path) {

        Profile fromProfile = profileRepository.findProfileByPath(path);

        Request request = requestRepository.findByFromProfile(fromProfile);

        Profile currentProfile = userDetails.getProfile();

        currentProfile.getRequests().remove(request);

        profileRepository.save(currentProfile);

        requestRepository.delete(request);
    }

    public long getRequestCount(Authentication authentication) {

        Profile profile = profileRepository.findProfileByHandle(authentication.getName());

        // TODO: Optimize this later.
        return profile.getRequests().size();
    }

    public List<Request> getSentRequests() {
        return requestRepository.findAllByFromProfile(userDetails.getProfile());
    }
}
