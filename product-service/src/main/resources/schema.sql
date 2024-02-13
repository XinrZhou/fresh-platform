create database if not exists `product-service`;
use `product-service`;
create table if not exists `category`
(
    id             bigint(19)   not null primary key,
    name           varchar(128) not null,
    image_url      mediumtext   not null comment '类目图片',
    parent_id      bigint(19)   not null default 0 comment '父类id，根类目为0',
    level          tinyint(1)   not null comment '类目层级，1，2，3',
    status         tinyint(1)   not null comment '状态，0未启用 1使用中',
    insert_time    datetime     not null default current_timestamp,
    update_time    datetime     not null default current_timestamp on update current_timestamp,
    index (parent_id),
    unique(name, parent_id)
) comment = '商品类目表';

create table if not exists `brand` (
    id          bigint(19)   not null primary key,
    name  varchar(128) not null,
    category_id bigint(19)   not null,
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp
) comment = '品牌表';

create table if not exists `attribute` (
    id          bigint(19)   not null primary key,
    category_id bigint(19)   not null,
    name        varchar(128) not null,
    is_numeric  tinyint(1)   not null default 0  comment '是否数字类型参数，0否，1是',
    unit        varchar(32)  default '' comment '数字类型的参数，非数字类型可为空',
    is_generic  tinyint(1)   not null default  1 comment '是否为sku通用属性，0否，1是',
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp,

    unique(category_id, name)
) comment = '规格参数表';

create table if not exists `spu` (
    id          bigint(19)   not null primary key,
    name        varchar(256) not null,
    title       varchar(256) not null default '',
    category_id bigint(19)   not null,
    brand_id    bigint(19)   not null,
    image_url   mediumtext   not null comment '产品主图',
    sale_status tinyint(1)   not null default '1' comment '是否上架，0下架，1上架',
    description text         null,
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp,

    unique (name, category_id)
) comment = 'SPU';

create table if not exists `sku`(
    id             bigint(19)     not null primary key,
    spu_id         bigint(20)     not null,
    name           varchar(256)   not null,
    image_url      mediumtext     not null,
    stock          int(8)         unsigned default '9999',
    origin_price   decimal(10, 2) not null default '0',
    discount_price decimal(10, 2) not null default '0',
    enable         tinyint(1)     not null default '1' comment '是否有效，0无效，1有效',
    generic_spec   json           null comment '通用规格参数数据',
    special_spec   json           null comment '特有规格参数及可选值信息',
    insert_time    datetime       not null default current_timestamp,
    update_time    datetime       not null default current_timestamp on update current_timestamp,

    unique(spu_id, name),
    index (spu_id)
) comment = 'SKU表';
