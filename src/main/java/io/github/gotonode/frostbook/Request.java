package io.github.gotonode.frostbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Request extends AbstractPersistable<Long> {

    //@Column(name = "from_user", nullable = false)
    @OneToOne
    private User fromUser;

    @Column(name = "date", nullable = false)
    private Date date;

}
