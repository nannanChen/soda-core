CREATE TABLE `point_info_0` (
  `row_key` varchar(30) NOT NULL DEFAULT '' COMMENT 'id',
  `name` varchar(20) DEFAULT NULL COMMENT '商圈名称',
  `date` varchar(8) DEFAULT NULL COMMENT '日期',
  `target_precursor` varchar(30) DEFAULT NULL COMMENT '目标点，前驱',
  `target_longitude` varchar(20) DEFAULT NULL COMMENT '目标点，经度',
  `target_latitude` varchar(20) DEFAULT NULL COMMENT '目标点，纬度',
  `target_next` varchar(30) DEFAULT NULL COMMENT '目标点，前驱',
  `distance` varchar(5) DEFAULT NULL COMMENT '当前点到商圈距离，单位米',
  `src_longitude` varchar(20) DEFAULT NULL COMMENT '源，经度',
  `src_latitude` varchar(20) DEFAULT NULL COMMENT '源，纬度',
  `src_next` varchar(30) DEFAULT NULL COMMENT '源，前驱'
  PRIMARY KEY (`row_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;