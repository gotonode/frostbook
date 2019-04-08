package io.github.gotonode.frostbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    public User add(String handle, String plaintextPassword, String name, String path) {

        // TODO: Hash this one!
        String password = plaintextPassword;

        User user = new User(handle, password, name, path);
        user.setDate(Date.from(Instant.now()));

        User output = userRepository.save(user);

        return output;
    }

    /**
     * Sends a friend request to this user.
     *
     * @param fromUserId The ID the request originates from.
     * @param toUserId   The ID of the target.
     */
    public void befriend(long fromUserId, long toUserId) {
        User fromUser = userRepository.getOne(fromUserId);
        User toUser = userRepository.getOne(toUserId);

        Request request = new Request();
        request.setDate(Date.from(Instant.now()));
        request.setFromUser(fromUser);

        toUser.getRequests().add(request);
    }

    /**
     * Confirms a friend request.
     *
     * @param fromUserId Who sent the request.
     * @param toUserId   To whom it was sent.
     */
    @Transactional
    public void accept(long fromUserId, long toUserId) {

        User fromUser = userRepository.getOne(fromUserId);
        User toUser = userRepository.getOne(toUserId);

        Request request = requestRepository.findByFromUser(fromUser);

        fromUser.getFriends().add(toUser);
        toUser.getFriends().add(fromUser);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        requestRepository.delete(request);

        // We delete this if it's found. This is the case where two users have
        // each sent a friend request to each other.
        Request secondRequest = requestRepository.findByFromUser(toUser);
        if (secondRequest != null) {
            requestRepository.delete(secondRequest);
        }
    }

    public List<User> find(String query) {
        return userRepository.findAllByHandleOrNameOrPathOrderByIdAsc(query, query, query);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public User findByPath(String path) {
        return userRepository.findUserByPath(path);
    }
}
