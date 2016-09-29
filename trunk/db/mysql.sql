CREATE TABLE `grid_divide_point_num` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` VARCHAR(20) DEFAULT NULL COMMENT '时刻',
  `index` VARCHAR(20) DEFAULT NULL COMMENT '网格方框下标',
  `longitude` VARCHAR(30) DEFAULT NULL COMMENT '经度',
  `latitude` VARCHAR(30) DEFAULT NULL COMMENT '纬度',
  `count` VARCHAR(5) DEFAULT NULL COMMENT '人数',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `point_map_info` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` VARCHAR(20) DEFAULT NULL COMMENT '时刻',
  `src_longitude` VARCHAR(30) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` VARCHAR(30) DEFAULT NULL COMMENT '源，纬度',
  `count` VARCHAR(5) DEFAULT NULL COMMENT '人数',
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
