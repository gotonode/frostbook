package io.github.gotonode.frostbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subcomment extends AbstractPersistable<Long> {

    //@Column(name = "from_user", nullable = false)
    @OneToOne
    private User fromUser;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date", nullable = false)
    private Date date;
}
