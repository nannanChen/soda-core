CREATE TABLE `grid_people_group1` (
  `id` INT(20) not null AUTO_INCREMENT,
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  `type` VARCHAR(10) DEFAULT NULL COMMENT '类型',
  `count` INT(15) DEFAULT NULL COMMENT '数量',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
ALTER TABLE `grid_people_group1` ADD INDEX grid_people_group_id_index ( `grid_people_group_id`);

CREATE TABLE `grid_from_to_num1` (
  `id` INT(20) not null AUTO_INCREMENT,
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` INT(2) DEFAULT NULL COMMENT '时刻',
  `from_index` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `to_index` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `count` INT(15) DEFAULT NULL COMMENT '人数',
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
ALTER TABLE `grid_from_to_num1` ADD INDEX date_hour_c_index ( `date`, `hour`,`count`);



CREATE TABLE `dbscan_point` (
  `id` INT(20) not null AUTO_INCREMENT,
  `clusterId` INT(20) DEFAULT NULL COMMENT '中心点',
  `x` VARCHAR(20) DEFAULT NULL COMMENT 'x',
  `y` VARCHAR(20) DEFAULT NULL COMMENT 'y',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;



CREATE TABLE `warn_average` (
  `id` INT(20) not null AUTO_INCREMENT,
  `grid_index` INT(20) DEFAULT NULL COMMENT '网格下标',
  `hour` INT(2) DEFAULT NULL COMMENT '小时',
  `avg` INT(15) DEFAULT NULL COMMENT '平均值',
  primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE `grid_imei_detail` (
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  `type` VARCHAR(10) DEFAULT NULL COMMENT '类型',
  `imei` VARCHAR(50) DEFAULT NULL COMMENT 'imei'
) ENGINE=MYISAM DEFAULT CHARSET=utf8;
# 使用MYISAM引擎
#16/10/27 11:20:53 INFO mapreduce.Job:  map 0% reduce 0%
#16/10/27 11:25:39 INFO mapreduce.Job:  map 100% reduce 0%
#4分钟46秒   入库100534520条数据  1亿


CREATE TABLE `grid_imei_detail1` (
  `grid_people_group_id` VARCHAR(50) DEFAULT NULL COMMENT '网格人分组id',
  `type` VARCHAR(10) DEFAULT NULL COMMENT '类型',
  `imei` VARCHAR(50) DEFAULT NULL COMMENT 'imei'
) ENGINE=INNODB DEFAULT CHARSET=utf8;
# 使用INNODB引擎
#16/10/27 11:28:14 INFO mapreduce.Job:  map 0% reduce 0%
#16/10/27 11:47:38 INFO mapreduce.Job:  map 100% reduce 0%
#19分钟24秒   入库100534520条数据  1亿

CREATE TABLE `predictData` (
  `count` INT(15) DEFAULT NULL COMMENT '人数',
  `hour` INT(2) DEFAULT NULL COMMENT '时刻',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `ind` INT(5) DEFAULT NULL COMMENT '网格方框下标'
) ENGINE=INNODB AUTO_INCREMENT=3897095 DEFAULT CHARSET=utf8


CREATE TABLE `predictManTpye` (
  `count` INT(15) DEFAULT NULL COMMENT '人数',
  `date` VARCHAR(8) DEFAULT NULL COMMENT '日期',
  `hour` INT(2) DEFAULT NULL COMMENT '时刻',
  `ind` INT(5) DEFAULT NULL COMMENT '网格方框下标',
  `type` INT(2) DEFAULT NULL COMMENT '类型'
) ENGINE=INNODB AUTO_INCREMENT=3897095 DEFAULT CHARSET=utf8

create table grid_group_all as
select DATE, HOUR, from_index,TYPE,sum(a.`count`) s from grid_people_group1 as a join grid_from_to_num1 as b on a.grid_people_group_id=b.grid_people_group_id
where (from_index = to_index  OR to_index = -1)AND from_index IN (180,157,226)
group by date, hour, type,from_index