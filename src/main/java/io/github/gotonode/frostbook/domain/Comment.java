package io.github.gotonode.frostbook.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "Comment.findAll", query="select c from Comment c order by c.date asc")
public class Comment extends AbstractPersistable<Long> {

    //@Column(name = "from_user", nullable = false)
    @OneToOne
    private Profile fromProfile;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToMany
    private List<Subcomment> subcomments = new ArrayList<>();

    @ManyToMany
    private List<Profile> likedBy = new ArrayList<>();

    @Override
    public String toString() {
        return "Comment{" +
                "fromProfile(handle)=" + fromProfile.getHandle() +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", subcomments(count)=" + subcomments.size() +
                ", likedBy(count)=" + likedBy.size() +
                '}';
    }
}
