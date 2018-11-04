# 所有字段都为not null，加快索引的速度
# tinyint范围为1~255
# datetime精确到秒，date精确到天
create table `house` (
  `id` bigint(20) unsigned not null auto_increment comment '主键id',
  `name` varchar(20) not null default '' comment '房产名称',
  `type` tinyint(1) not null default '0' comment '1：销售，2：出租',
  `price` int(11) not null comment '单位元',
  `images` varchar(1024) not null default '' comment '图片地址',
  `area` int(11) not null default '0' comment '面积',
  `beds` int(11) not null default '0' comment '卧室数量',
  `baths` int(11) not null default '0' comment '卫生间数量',
  `rating`double not null default '0' comment '评级',
  `remarks` varchar(512) not null default '' comment '房产描述',
  `properties` varchar(512) not null default '' comment '属性',
  `floor_plan` varchar(255) not null default '' comment '户型图',
  `tags` varchar(20) not null default '' comment '标签',
  `create_time` datetime not null comment '创建时间',
  `city_id` int(11) not null default '0' comment '城市id',
  `community_id` int(11) not null default '0' comment '小区id',
  `address` varchar(20) not null default '' comment '房产地址',
  `state` tinyint(1) not null default '1' comment '1-上架 2-下架',
  primary key (`id`)
) engine=InnoDB auto_increment=25 default charset=utf8;

# 小区表
create table `community` (
  `id` int(11) unsigned not null auto_increment,
  `city_code` varchar(11) not null default '' comment '城市编码',
  `name` varchar(50) not null default '' comment '小区名称',
  `city_name` varchar(11) not null default '' comment '城市名称',
  primary key (`id`)
)engine=InnoDB auto_increment=8 default charset=utf8;

# 用户表
create table `user` (
  `id` bigint(20) unsigned not null auto_increment comment '主键',
  `name` varchar(20) not null default '' comment '姓名',
  `phone` char(13) not null default '' comment '手机号',
  `email` varchar(90) not null default '' comment '电子邮件',
  `aboutme` varchar(255) not null default '' comment '自我介绍',
  `passwd` varchar(1024) not null default '' comment '经过MD5加盐加密密码',
  `avatar` varchar(512) not null default '' comment '头像地址',
  `type` tinyint(1) not null default '1' comment '1：普通用户，2：房产经纪人',
  `create_time` datetime not null comment '创建时间',
  `enable` tinyint(1) not null default '0' comment '是否启用 1：启用 0：停用',
  `agency_id` int(11) not null default '0' comment '所属经纪机构',
  primary key (`id`),
  unique key `idx_email` (`email`),
  unique key `idx_name` (`name`),
  unique key `idx_phone` (`phone`)
)engine=InnoDB auto_increment=25 default charset=utf8;

# 房产用户表
create table `house_user` (
  `id` bigint(20) unsigned not null auto_increment,
  `house_id` bigint(20) not null comment '房屋id',
  `user_id` bigint(20) not null comment '用户id',
  `create_time` datetime not null comment '创建时间',
  `type` tinyint(1) not null comment '1-售卖 2-收藏',
  primary key (`id`),
  #防止一个用户收藏或代理售卖房屋多次
  unique key `house_id_user_id_type` (`house_id`, `user_id`, `type`)
)engine=InnoDB auto_increment=20 default charset=utf8;

# 房产留言表
create table `house_msg`(
  `id` bigint(20) unsigned not null auto_increment,
  `msg` varchar(1024) not null default '' comment '消息',
  `create_time` datetime not null comment '创建时间',
  `agent_id` bigint(20) not null comment '经纪人id',
  `house_id` bigint(20) not null comment '房屋id',
  `user_name` varchar(20) not null default '' comment '用户姓名',
  primary key (`id`)
)engine=InnoDB auto_increment=7 default charset=utf8;

# 经纪机构表
create table `agency` (
  `id` int(11) unsigned not null auto_increment,
  `name` varchar(20) not null default '' comment '经纪机构名称',
  `address` varchar(100) not null default '' comment '地址',
  `phone` varchar(30) not null default '' comment '手机',
  `email` varchar(50) not null default '' comment '邮箱',
  `about_us` varchar(100) not null default '' comment '描述',
  `mobile` varchar(11) not null default '' comment '电话',
  `web_site` varchar(20) not null default '' comment '网站',
  primary key (`id`)
)engine=InnoDB auto_increment=5 default charset=utf8;

# 百科表/博客表
create table `blog`(
  `id` int(11) unsigned not null auto_increment,
  `tags` varchar(20) not null default '' comment '标签',
  `content` text not null comment '内容',
  `create_time` datetime not null comment '日期',
  `title` varchar(20) not null default '' comment '标题',
  `cat` int(11) not null default '0' comment '分类1-准备买房 2-看房/选房 3-签约/订房 4-全款/贷款 5-缴税/过户 6-入住/交接 7-买房风险',
  primary key (`id`)
)engine=InnoDB auto_increment=7 default charset=utf8;

# 评论表
# 房屋和博客都可以评论
create table `comment` (
  `id` bigint(20) unsigned not null auto_increment,
  `content` varchar(512) not null default '' comment '评论内容',
  `house_id` bigint(20) not null comment '房屋id',
  `blog_id` int(11) not null comment '博客id',
  `type` tinyint(1) not null comment '类型1-房产评论 2-博客评论',
  `user_id` bigint(20) not null comment '评论用户',
  `create_time` datetime not null comment '发布时间戳',
  primary key (`id`)
)engine=InnoDB auto_increment=17 default charset=utf8;

# 城市表
create table `city` (
  `id` int(11) unsigned not null auto_increment,
  `city_name` varchar(11) not null default '' comment '城市名称',
  `city_code` varchar(10) not null default '' comment '城市编码',
  primary key (`id`)
)engine=InnoDB default charset=utf8;
 
# 创建数据库并指定编码集
# CREATE DATABASE `houses` CHARACTER SET utf8 COLLATE utf8_general_ci;

desc house;
desc house_user;
desc house_msg;
desc user;
desc community;
desc comment;
desc blog;


show create table house_user;
show index from house_user;

alter table user add column salt char(48) not null after passwd;

insert into user (id, name, phone, email, aboutme, passwd, salt, avatar, type, create_time, `enable`, agency_id)
    values
      (8,'hello992','hello2','spring_boot2@163.com','2255','75fb23b165249cedeb60544c4095ec99', 'CPTPgTlgVkt/nomDxaR3xmY50DbV0w3EdvVseGsIaiOji1Q', '/1493438523/4.jpg',2,'2017-01-31',1,1)

select *
from user;

delete from user where id in ('7', '8');