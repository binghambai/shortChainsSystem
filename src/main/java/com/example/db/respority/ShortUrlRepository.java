package com.example.db.respority;

import com.example.db.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long>, JpaSpecificationExecutor<ShortUrl> {

    List<ShortUrl> findBySourceUr(String sourceUrl);

    boolean existsById(Long id);
}