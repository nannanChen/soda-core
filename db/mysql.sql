CREATE TABLE `grid_people_group3` (
  `id` INT(20) not null AUTO_INCREMENT,
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  `type` VARCHAR(10) DEFAULT NULL COMMENT '类型',
  `count` INT(15) DEFAULT NULL COMMENT '数量',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
ALTER TABLE `grid_people_group3` ADD INDEX grid_people_group_id_index ( `grid_people_group_id`);

CREATE TABLE `grid_from_to_num3` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` INT(2) DEFAULT NULL COMMENT '时刻',
  `from_index` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `to_index` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `count` INT(15) DEFAULT NULL COMMENT '人数',
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
ALTER TABLE `grid_from_to_num3` ADD INDEX date_hour_index ( `date`, `hour`);