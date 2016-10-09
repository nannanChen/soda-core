CREATE TABLE `grid_people_group2` (
  `id` INT(20) not null AUTO_INCREMENT,
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  `type` VARCHAR(10) DEFAULT NULL COMMENT '类型',
  `count` INT(15) DEFAULT NULL COMMENT '数量',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
ALTER TABLE `grid_people_group2` ADD INDEX grid_people_group_id_index ( `grid_people_group_id`);

CREATE TABLE `grid_from_to_num2` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` INT(2) DEFAULT NULL COMMENT '时刻',
  `from_index` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `to_index` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `count` INT(15) DEFAULT NULL COMMENT '人数',
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
ALTER TABLE `grid_from_to_num2` ADD INDEX date_hour_index ( `date`, `hour`);




CREATE TABLE `grid_divide_point_num2` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` INT(2) DEFAULT NULL COMMENT '时刻',
  `index` INT(20) DEFAULT NULL COMMENT '网格方框下标',
  `longitude` VARCHAR(30) DEFAULT NULL COMMENT '经度',
  `latitude` VARCHAR(30) DEFAULT NULL COMMENT '纬度',
  `count` INT(20) DEFAULT NULL COMMENT '人数',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `point_map_info` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` INT(20) DEFAULT NULL COMMENT '时刻',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `count` INT(20) DEFAULT NULL COMMENT '人数',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;





CREATE TABLE `point_info_0` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `point_info_1` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;



CREATE TABLE `point_info_7` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_8` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `point_info_9` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_10` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_11` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_12` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_13` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_14` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_15` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_16` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_17` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_18` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_19` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_20` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_21` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_22` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `point_info_23` (
  `row_key` VARCHAR(30) NOT NULL COMMENT 'id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '商圈名称',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` VARCHAR(30) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` VARCHAR(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` VARCHAR(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `src_next` VARCHAR(30) DEFAULT NULL COMMENT '源，前驱',
  PRIMARY KEY (`row_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
