create database if not exists `aigc-service`;
use `aigc-service`;

create table if not exists `model`
(
    id             bigint(19)   not null primary key,
    version        varchar(65)  not null,
    type           tinyint(1)   not null comment '类型，0绘画 1写作',
    status         tinyint(1)   not null default 1 comment '状态，0未使用 1使用中',
    params         json         not null comment '模型参数',
    insert_time    datetime     not null default current_timestamp,
    update_time    datetime     not null default current_timestamp on update current_timestamp,

    unique(version, type),
    index(type)
) comment = '模型参数表';

create table if not exists `resource`
(
    id           bigint(19)     not null primary key,
    name         varchar(255)   not null,
    type         tinyint(1)     not null comment '资源类型，0绘画 1写作',
    status       tinyint(1)     not null default 1 comment '状态，0禁用 1使用',
    description  varchar(600)   null,
    price        decimal(10, 2) null,
    unit         varchar(65)    null,
    insert_time  datetime       not null default current_timestamp,
    update_time  datetime       not null default current_timestamp on update current_timestamp,

    unique(name, type)
) comment = '资源表';