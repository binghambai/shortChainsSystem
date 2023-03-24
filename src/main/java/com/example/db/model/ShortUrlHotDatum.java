package com.example.db.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "short_url_hot_data")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlHotDatum implements Serializable {
    @Id
    @Column(name = "short_id", nullable = false)
    private Long id;

    @Column(name = "source_id", nullable = false)
    private String sourceId;

    @Column(name = "count")
    private Long count;

    @Column(name = "recently_access_time", nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime recentlyAccessTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShortUrlHotDatum that = (ShortUrlHotDatum) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}