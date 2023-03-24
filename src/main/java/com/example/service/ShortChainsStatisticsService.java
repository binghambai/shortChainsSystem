package com.example.service;

import com.example.db.model.ShortUrl;
import com.example.db.model.ShortUrlHotDatum;
import com.example.db.respority.ShortUrlHotDatumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ShortChainsStatisticsService {

    @Autowired
    private ShortUrlHotDatumRepository shortUrlHotDatumRepository;

    public void statisticsShortUrl(ShortUrl shortUrl) {
        if (shortUrl.isEmpty()) {
            System.out.println("待统计数据为空，无需统计");
            return;
        }
        ShortUrlHotDatum shortUrlHotDatum = ShortUrlHotDatum.builder()
                .id(shortUrl.getId())
                .sourceId(shortUrl.getSourceUr())
                .recentlyAccessTime(LocalDateTime.now())
                .build();

        ShortUrlHotDatum byShortId = shortUrlHotDatumRepository.findByShortId(shortUrl.getId());
        if (byShortId == null) {
            shortUrlHotDatumRepository.save(shortUrlHotDatum);
            return;
        }
        //count + 1以后再填入
        shortUrlHotDatum.setCount(byShortId.getCount() + 1);
        shortUrlHotDatum.setRecentlyAccessTime(LocalDateTime.now());
        shortUrlHotDatumRepository.save(shortUrlHotDatum);
    }

    public void begin() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0)
                .minus(1, ChronoUnit.DAYS);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        shortUrlHotDatumRepository.findByRangeTime(start, end);
    }
}
