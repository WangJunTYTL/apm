CREATE TABLE IF NOT EXISTS APM_TIMING_STATISTICS (
  `tag`         VARCHAR(255) NOT NULL,
  `count`       INT         DEFAULT 0,
  `mean`        DOUBLE      DEFAULT 0,
  `min`         BIGINT      DEFAULT 0,
  `max`         BIGINT      DEFAULT 0,
  `std`         DOUBLE      DEFAULT 0,
  `interval`    INT         DEFAULT 0,
  `hostname`    VARCHAR(100) DEFAULT 'Local',
  `create_time` BIGINT       NOT NULL
);

CREATE INDEX IF NOT EXISTS index_APM_TIMING_STATISTICS_tag ON APM_TIMING_STATISTICS (tag);
CREATE INDEX IF NOT EXISTS index_APM_TIMING_STATISTICS_create_time ON APM_TIMING_STATISTICS   (create_time);