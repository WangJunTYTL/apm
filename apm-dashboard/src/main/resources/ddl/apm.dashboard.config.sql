CREATE TABLE IF NOT EXISTS t_graph_config (
  `title`        VARCHAR(50)  NOT NULL,
  `sub_title`    VARCHAR(50) DEFAULT "",
  `metrics_type` VARCHAR(10),
  `tags`         VARCHAR(100) NOT NULL,
  `clusters`     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_node_config (
  `name`    VARCHAR(20) NOT NULL,
  `url`     VARCHAR(50) NOT NULL,
  `cluster` VARCHAR(20) NOT NULL
);
