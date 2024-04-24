create database if not exists `configuration-service`;
use `configuration-service`;

create table if not exists page
(
    id           bigint(19)   not null primary key,
    name         varchar(65)  not null,
    settings     json         null,
    insert_time  datetime     not null default current_timestamp,
    update_time  datetime     not null default current_timestamp on update current_timestamp
);

create table if not exists marketing_activity
(
    id           bigint(19)   not null primary key,
    title        varchar(255) not null,
    tag          varchar(65)  not null,
    content      mediumtext   not null,
    start_time   varchar(65)  not null,
    end_time     varchar(65)  not null,
    status       tinyint(1)   not null default '1',
    insert_time  datetime     not null default current_timestamp,
    update_time  datetime     not null default current_timestamp on update current_timestamp
);

create table if not exists marketing_advertisement
(
    id           bigint(19)     not null primary key,
    title        varchar(255)   not null,
    price        decimal(10, 2) not null default '0',
    unit         varchar(65)     not null,
    description  varchar(255)   null,
    insert_time  datetime       not null default current_timestamp,
    update_time  datetime       not null default current_timestamp on update current_timestamp
);
