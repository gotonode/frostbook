package io.github.gotonode.frostbook;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class User extends AbstractPersistable<Long> {

    @Column(name = "handle", nullable = false, unique = true)
    private String handle;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false, unique = true)
    private String path;

    @ManyToMany
    private List<User> friends;

    @ManyToMany
    private List<Request> requests;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "images", nullable = false)
    @OneToMany
    private List<Image> images;

    //@Column(name = "profile_image", nullable = true)
    @OneToOne
    private Image profileImage;

    @ManyToMany
    private List<Comment> comments;

    public User() {
    }

    public User(String handle, String password, String name, String path) {
        this.handle = handle;
        this.password = password;
        this.name = name;
        this.path = path;
    }

    @Override
    public String toString() {
        return "User{" +
                "handle='" + handle + '\'' +
                ", password='" + String.valueOf("REDACTED") + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", date=" + date +
                ", profileImage=" + profileImage +
                '}';
    }
}
