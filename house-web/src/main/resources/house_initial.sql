alter table house add column rating_nums int(11) not null default 0 after rating;
alter table house modify column area double(6,2) not null default 0.0 comment '房产面积';
alter table house modify column name varchar(40) not null default '' comment '房产名字';
alter table agency modify column web_site varchar(50) not null default '' comment '网站地址';
# 修改comment表content列字符集
alter table comment modify content text character set utf8mb4 collate utf8mb4_unicode_ci comment '评论内容';
insert into agency (name, address, phone, email, about_us, mobile, web_site)
values
  ('中原地产上海1','中原地产上海1地址','021-89898980','010@gmail.com','中原地产上海','0211','www.centanet.com'),
  ('交点房产','交点房产地址','010-87898989','020@gmail.com','1','1','1'),
  ('中原地产北京1','中原地产北京1地址','010-89898960','010@gmail.com','中原地产北京','0101','www.centanet.com'),
  ('唛田','唛田地址','010-88898989','030@gmail.com','1','1','1'),
  ('中原地产上海2','中原地产上海2地址','021-89898981','010@gmail.com','中原地产上海','0212','www.centanet.com'),
  ('安聚客','安聚客地址','010-81898989','040@gmail.com','1','1','1'),
  ('链家房产','链家房产','010-45674567','linke@163.com','链家房产','12090909090', 'www.link.com'),
  ('中原地产南京1','中原地产南京1地址','022-89898970','010@gmail.com','中原地产南京','0221','www.centanet.com');

insert into city (city_code, city_name) values
  ('110000', '北京'),
  ('120000', '上海'),
  ('130000', '南京'),
  ('140000', '杭州'),
  ('150000', '成都'),
  ('160000', '广州');

insert into community(city_code, city_name) select city_code, city_name from city;

update community
set name = case id
           when 1 then '士林花苑'
           when 2 then '世博家园'
           when 3 then '华侨城'
           when 4 then '一品漫城'
           when 5 then '汇龙新城'
           when 6 then '中治锦城'
           end
where id < 7;

insert into community (city_code, name, city_name) values
  ('120000', '光复新苑', '上海'),
  ('120000', '上海绿城', '上海'),
  ('120000', '静安豪景苑', '上海'),
  ('120000', '安图新村', '上海'),
  ('120000', '中环一号', '上海'),
  ('120000', '仁恒河滨城', '上海'),
  ('120000', '百汇园', '上海');
