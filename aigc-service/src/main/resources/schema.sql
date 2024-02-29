create database if not exists `ai-service`;
use `ai-service`;

create table if not exists resource
(
    id          bigint(19) not null primary key,
    user_id     bigint(19) not null,
    type        tinyint(1) not null,
    insert_time datetime   not null default current_timestamp,
    update_time datetime   not null default current_timestamp on update current_timestamp
);