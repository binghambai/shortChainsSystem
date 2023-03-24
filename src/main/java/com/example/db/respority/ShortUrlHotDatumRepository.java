package com.example.db.respority;

import com.example.db.model.ShortUrlHotDatum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShortUrlHotDatumRepository extends JpaRepository<ShortUrlHotDatum, Long>
        , JpaSpecificationExecutor<ShortUrlHotDatum> {

    @Query(value = "from ShortUrlHotDatum where id = ?1")
    ShortUrlHotDatum findByShortId(Long shortId);

    @Query(value = "from ShortUrlHotDatum where recentlyAccessTime > ?1 and recentlyAccessTime < ?2")
    List<ShortUrlHotDatum> findByRangeTime(LocalDateTime start, LocalDateTime end);
}