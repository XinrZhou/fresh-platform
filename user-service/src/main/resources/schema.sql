create database if not exists `user-service`;
use `user-service`;

create table if not exists user
(
    id           bigint(19)   not null primary key,
    name         varchar(65)  not null,
    number       varchar(11)  not null,
    password     varchar(65)  not null,
    role         int          not null,
    avatar       varchar(256) null,
    supplier     json         null comment '{"shopStatus", "rdcId"}',
    consumer     json         null comment '{"isVip", "balance"}',
    insert_time  datetime     not null default current_timestamp,
    update_time  datetime     not null default current_timestamp on update current_timestamp,

    index (number),
    index (role)
);

create table if not exists rdc
(
    id          bigint(19)   not null primary key,
    name        varchar(256) not null,
    province    varchar(65)  null,
    city        varchar(65)  null,
    district    varchar(65)  null,
    detail      varchar(256) null,
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp
);

create table if not exists address
(
    id           bigint(19)  not null primary key,
    user_id      bigint(19)  not null,
    rdc_id       bigint(19)  not null,
    phone_number varchar(11) not null,
    contact      varchar(65) not null,
    is_default   int         not null,
    insert_time  datetime    not null default current_timestamp,
    update_time  datetime    not null default current_timestamp on update current_timestamp
);
