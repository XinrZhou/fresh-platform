create database if not exists `cart-service`;
use `cart-service`;

create table if not exists cart
(
    id          bigint(19)  not null primary key,
    user_id     bigint(19)  not null,
    sku_id      bigint(19)  not null,
    count       tinyint(5)  not null default 1,
    type        tinyint(1)  not null default 0,
    insert_time datetime    not null default current_timestamp,
    update_time datetime    not null default current_timestamp on update current_timestamp
);

create table if not exists `saleOrder`
(
    id           bigint(19)    not null primary key,
    user_id      bigint(19)    not null,
    address_spec json          not null,
    order_spec   json          not null,
    type         tinyint(1)    not null default 0 comment '订单类型 0配送 1自提',
    status       tinyint(1)    not null default 1 comment '订单状态 0待付款 1待发货 2待收货 3待评价',
    price        decimal(10,2) not null,
    remark       varchar(255)  null,
    insert_time  datetime      not null default current_timestamp,
    update_time  datetime      not null default current_timestamp on update current_timestamp
);

create table if not exists `order_sku`
(
    id           bigint(19)    not null primary key,
    order_id     bigint(19)    not null,
    sku_id       bigint(19)    not null,
    ext_spec     json          not null,
    status       tinyint(1)    not null default 0 comment '商品状态 0未发货 1已发货',
    insert_time  datetime      not null default current_timestamp,
    update_time  datetime      not null default current_timestamp on update current_timestamp
);
