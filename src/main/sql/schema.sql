-- 创建数据库
create database seckill;

-- 使用数据库
use seckill;

-- 创建秒杀库存表
create table goods(
 `id` bigint not null auto_increment comment '商品库存id',
 `name` varchar(120) not null comment '商品名称',
 `number` int not null comment '库存数量',
 `start_time` timestamp not null comment '秒杀开始时间',
 `end_time` timestamp not null comment '秒杀结束时间',
 `create_time` timestamp not null default current_timestamp comment '创建时间',
    primary key(id),
    key idx_start_time(start_time),
    key idx_end_time(end_time),
    key idx_create_time(create_time)
) engine=innodb auto_increment=1000 default charset=utf8 comment ='秒杀库存表';

-- 初始化数据
insert into
    goods(name, number, start_time, end_time)
values
    ('10000元秒杀iPhone', 100, '2020-12-26 00:00:00', '2020-12-26 00:00:00'),
    ('500元秒杀iPad', 200, '2020-12-26 00:00:00', '2020-12-26 00:00:00'),
    ('300元秒杀小米', 300, '2020-12-26 00:00:00', '2020-12-26 00:00:00'),
    ('200元秒杀红米', 400, '2020-12-26 00:00:00', '2020-12-26 00:00:00');


-- 秒杀成功明细表

create table goods_killed(
    `goods_id` bigint not null comment '秒杀商品id',
    `user_phone` bigint not null comment '用户手机号',
    `state` tinyint not null default  -1 comment '状态：-1：无效 0：成功 1：已付款',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
    primary key(goods_id, user_phone), -- 联合主键
    key idx_create_time(create_time)
) engine=innodb  default charset=utf8 comment ='秒杀成功明细';



-- 连接数据库
-- mysql -uroot -p














