create database if not exists `user-service`;
use `user-service`;

create table if not exists user
(
    id bigint(19) not null primary key,
    user_name varchar(65) not null,
    password varchar(65) not null,
    role int not null,
    phone_number varchar(11) not null,
    is_vip int,
    balance decimal,
    insert_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp,
    index (phone_number),
    index (role),
    unique (phone_number)
);

create table if not exists address
(
    id bigint(19) not null primary key,
    user_id bigint(19) not null,
    province varchar(65),
    city varchar(65),
    town varchar(65),
    street varchar(65),
    phone_number varchar(11) not null,
    contact varchar(65) not null,
    is_default int not null,
    insert_time datetime not null default current_timestamp,
    update_time datetime not null default current_timestamp on update current_timestamp
);
