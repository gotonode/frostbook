package io.github.gotonode.frostbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends AbstractPersistable<Long> {

    //@Column(name = "from_user", nullable = false)
    @OneToOne
    private Profile fromProfile;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToMany
    private List<Subcomment> subcomments;

    @ManyToMany
    private List<Profile> likedBy;
}
