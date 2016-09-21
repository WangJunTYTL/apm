CREATE TABLE IF NOT EXISTS APM_ALERT (
  `id`              INT PRIMARY KEY AUTO_INCREMENT,
  `service`         VARCHAR(50)  NOT NULL,
  `tag`             VARCHAR(100) NOT NULL,
  `term`            VARCHAR(255) NOT NULL,
  `interval`        INT             DEFAULT 1,
  `status`          INT             DEFAULT 0,
  `sms_msg`         VARCHAR(1000),
  `mail_msg`        TEXT,
  `receiver_groups` VARCHAR(255) NOT NULL,
  `create_time`     DATETIME         NOT NULL,
  `update_time`     DATETIME         NOT NULL,
  CONSTRAINT ServiceTag UNIQUE (`service`,`tag`),
  INDEX (tag)
);
