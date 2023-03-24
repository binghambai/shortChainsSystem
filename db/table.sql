CREATE TABLE `short_url_hot_data` (
    `short_id` bigint NOT NULL COMMENT '短链id\n',
    `source_id` varchar(255) NOT NULL COMMENT '原始id',
    `count` bigint DEFAULT '0' COMMENT '访问次数',
    `recently_access_time` datetime NOT NULL COMMENT '最近访问时间',
    PRIMARY KEY (`short_id`),
    KEY `idx_recently_time` (`recently_access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='热点数据热Key统计';

CREATE TABLE `short_url` (
    `id` bigint NOT NULL COMMENT '雪花算法生成的短号id',
    `source_ur` varchar(255) NOT NULL COMMENT '原链接地址',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='短链地址映射表';