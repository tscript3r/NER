package pl.tscript3r.ner.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Getter
    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    public Boolean isNew() {
        return id == null;
    }

    private boolean identityEquals(final AbstractEntity other) {
        if (isNew())
            return false;
        return getId().equals(other.getId());
    }

    private int identityHashCode() {
        return new HashCodeBuilder().append(this.getId()).toHashCode();
    }

    @Override
    public final int hashCode() {
        return identityHashCode();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        return identityEquals((AbstractEntity) o);
    }

}
