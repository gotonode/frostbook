package io.github.gotonode.frostbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Profile extends AbstractPersistable<Long> {

    @Column(name = "handle", nullable = false, unique = true)
    private String handle;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false, unique = true)
    private String path;

    @ManyToMany
    private List<Profile> friends = new ArrayList<>();

    @ManyToMany
    private List<Request> requests = new ArrayList<>();

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "images", nullable = false)
    @OneToMany
    private List<Image> images = new ArrayList<>();

    //@Column(name = "profile_image", nullable = true)
    @OneToOne
    private Image profileImage;

    @ManyToMany
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities = new ArrayList<>();

    public Profile(String handle, String password, String name, String path) {
        this.handle = handle;
        this.password = password;
        this.name = name;
        this.path = path;
    }

    public Profile() {

    }

    @Override
    public String toString() {
        return "Profile{" +
                "handle='" + handle + '\'' +
                ", password='" + String.valueOf("[PROTECTED]") + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", friends(count)=" + friends.size() +
                ", requests(count)=" + requests.size() +
                ", date=" + date +
                ", images(count)=" + images.size() +
                ", profileImage=" + profileImage +
                ", comments(count)=" + comments.size() +
                ", authorities=" + String.valueOf("[PROTECTED]") +
                '}';
    }

    public List<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {

        // TODO: Use Java's stream here.
        List<SimpleGrantedAuthority> output = new ArrayList<>();

        for (String authority : this.getAuthorities()) {
            output.add(new SimpleGrantedAuthority(authority));
        }

        return output;
    }
}
