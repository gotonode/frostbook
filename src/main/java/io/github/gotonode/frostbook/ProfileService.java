package io.github.gotonode.frostbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Profile add(String handle, String plaintextPassword, String name, String path) {

        // TODO: Hash this one!
        String password = plaintextPassword;

        Profile profile = new Profile(handle, password, name, path);
        profile.setDate(Date.from(Instant.now()));

        Profile output = profileRepository.save(profile);

        return output;
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

    /**
     * Confirms a friend request.
     *
     * @param fromProfileId Who sent the request.
     * @param toProfileId   To whom it was sent.
     */
    @Transactional
    public void accept(long fromProfileId, long toProfileId) {

        Profile fromProfile = profileRepository.getOne(fromProfileId);
        Profile toProfile = profileRepository.getOne(toProfileId);

        Request request = requestRepository.findByFromProfile(fromProfile);

        fromProfile.getFriends().add(toProfile);
        toProfile.getFriends().add(fromProfile);

        profileRepository.save(fromProfile);
        profileRepository.save(toProfile);

        requestRepository.delete(request);

        // We delete this if it's found. This is the case where two users have
        // each sent a friend request to each other.
        Request secondRequest = requestRepository.findByFromProfile(toProfile);
        if (secondRequest != null) {
            requestRepository.delete(secondRequest);
        }
    }

    public List<Profile> find(String query) {
        return profileRepository.findAllByHandleOrNameOrPathContainingOrderByIdAsc(query, query, query);
    }

    public List<Profile> findAll() {
        return profileRepository.findAllByOrderByIdAsc();
    }

    public Profile findByPath(String path) {
        return profileRepository.findProfileByPath(path);
    }
}
