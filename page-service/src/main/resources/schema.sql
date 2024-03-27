create database if not exists `page-service`;
use `page-service`;

create table if not exists page
(
    id           bigint(19)   not null primary key,
    name         varchar(65)  not null,
    settings     json         null,
    insert_time  datetime     not null default current_timestamp,
    update_time  datetime     not null default current_timestamp on update current_timestamp
);
