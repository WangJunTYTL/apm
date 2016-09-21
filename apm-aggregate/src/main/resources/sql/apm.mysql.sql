CREATE TABLE IF NOT EXISTS APM_TIMING_STATISTICS (
  `tag`         VARCHAR(255) NOT NULL,
  `count`       INT         DEFAULT 0,
  `mean`        DOUBLE      DEFAULT 0,
  `min`         BIGINT      DEFAULT 0,
  `max`         BIGINT      DEFAULT 0,
  `std`         DOUBLE      DEFAULT 0,
  `interval`    INT         DEFAULT 0,
  `hostname`     VARCHAR(100) DEFAULT 'Unknown',
  `create_time` BIGINT       NOT NULL,
  INDEX (tag),
  INDEX (hostname),
  INDEX (create_time)
);

CREATE TABLE IF NOT EXISTS APM_SERVICE (
  `service`  VARCHAR(100) NOT NULL PRIMARY KEY ,
  `hostname` VARCHAR(100)
)