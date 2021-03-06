package io.github.gotonode.frostbook.domain;

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

    @OneToOne
    private Profile fromProfile;

    @Column(name = "date", nullable = false)
    private Date date;

    @Override
    public String toString() {
        return "Request{" +
                "fromProfile(handle)=" + fromProfile.getHandle() +
                ", date=" + date +
                '}';
    }
}
