package io.github.gotonode.frostbook.domain;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

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
    private List<Profile> friends;

    @ManyToMany
    private List<Request> requests;

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
                ", password='" + String.valueOf("REDACTED") + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", date=" + date +
                ", profileImage=" + profileImage +
                '}';
    }
}
