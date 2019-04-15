package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String handle) throws UsernameNotFoundException {

        Profile profile = profileRepository.findProfileByHandle(handle);

        if (profile == null) {
            throw new UsernameNotFoundException("No such handle: " + handle);
        }

        List<String> authorities = profile.getAuthorities();

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();

        for (String authority : authorities) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority((authority)));
        }

        User user = new User(
                profile.getHandle(),
                profile.getPassword(),
                true,
                true,
                true,
                true,
                simpleGrantedAuthorities);

        System.out.println("User login details: " + user);

        return user;
    }
}
