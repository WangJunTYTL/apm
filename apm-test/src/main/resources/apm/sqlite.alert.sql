-- sqlite

CREATE TABLE IF NOT EXISTS APM_ALERT_CONF (
  `id`              INTEGER PRIMARY KEY AUTOINCREMENT,
  `tag`             VARCHAR(255) UNIQUE NOT NULL,
  `term`            VARCHAR(1000),
  `status`          INT                 DEFAULT 0,
  `interval`        INT                 DEFAULT 1,
  `sms_msg`         TEXT                DEFAULT NULL,
  `email_msg`       TEXT                DEFAULT NULL,
  `receiver_groups` VARCHAR(255),
  `verify_count`    INT                 DEFAULT 0,
  `trigger_count`   INT                 DEFAULT 0,
  `version`         INT                 DEFAULT 0,
  `create_time`     BIGINT              NOT NULL,
  `update_time`     BIGINT              NOT NULL
);
-- 规则标签
-- 规则表达式
-- 状态
-- 检测间隔周期，单位分钟
-- 短信消息
-- 邮件消息
-- 接收用户群组
-- 已检测次数
-- 已触发次数
-- 配置版本号
-- 创建时间
-- 更新时间

CREATE INDEX IF NOT EXISTS INDEX_APM_ALERT_TAG
  ON APM_ALERT_CONF (tag);