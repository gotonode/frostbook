package io.github.gotonode.frostbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Image extends AbstractPersistable<Long> {

    @Column(name = "data", nullable = false)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.ImageType")
    @JsonIgnore
    private byte[] data;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "length", nullable = false)
    private long length;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToMany
    private List<Profile> likedBy = new ArrayList<>();

    @ManyToMany
    private List<Subcomment> subcomments = new ArrayList<>();

    @Override
    public String toString() {
        return "Image{" +
                "data=" + String.valueOf("[REDACTED]") +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", length=" + length +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", likedBy(count)=" + likedBy.size() +
                ", subcomments(count)=" + subcomments.size() +
                '}';
    }
}
