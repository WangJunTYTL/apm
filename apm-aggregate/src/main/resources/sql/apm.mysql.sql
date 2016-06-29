CREATE TABLE IF NOT EXISTS APM_TIMING_STATISTICS (
  `tag`         VARCHAR(255) NOT NULL,
  `count`       INT         DEFAULT 0,
  `mean`        DOUBLE      DEFAULT 0,
  `min`         BIGINT      DEFAULT 0,
  `max`         BIGINT      DEFAULT 0,
  `std`         DOUBLE      DEFAULT 0,
  `interval`    INT         DEFAULT 0,
  `hostname`    VARCHAR(20) DEFAULT 'Local',
  `create_time` BIGINT       NOT NULL,
  INDEX (tag),
  INDEX (hostname),
  INDEX (create_time)
);

