package io.github.gotonode.frostbook.domain;

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

    /**
     * Displays information about the object.
     * <p>
     * Note that it's somewhat okay to print the password as it is already hashed.
     *
     * @return A String with all the data.
     */
//    @Override
//    public String toString() {
//        return "Profile{" +
//                "handle='" + handle + '\'' +
//                ", password='" + password + '\'' +
//                ", name='" + name + '\'' +
//                ", path='" + path + '\'' +
//                ", friends=" + friends +
//                ", requests=" + requests +
//                ", date=" + date +
//                ", images=" + images +
//                ", profileImage=" + profileImage +
//                ", comments=" + comments +
//                ", authorities=" + authorities +
//                '}';
//    }

    public List<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {

        // TODO: Use Java's stream here.
        List<SimpleGrantedAuthority> output = new ArrayList<>();

        for (String authority : this.getAuthorities()) {
            output.add(new SimpleGrantedAuthority(authority));
        }

        return output;
    }
}
