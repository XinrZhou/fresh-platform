create database if not exists `product-service`;
use `product-service`;

create table if not exists `category`
(
    id            bigint(19)   not null primary key,
    category_name varchar(128) not null,
    parent_id     bigint(19)   not null comment '父类id，根类目为0',
    is_parent     tinyint(1)   not null comment '是否为父节点，0否，1是',
    insert_time   datetime     not null default current_timestamp,
    update_time   datetime     not null default current_timestamp on update current_timestamp,

    index (parent_id)
) comment = '商品类目表';

create table if not exists `brand` (
    id          bigint(19)   not null primary key,
    brand_name  varchar(128) not null,
    image_url   text,
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp
) comment = '品牌表';

create table if not exists `category_brand` (
    id           bigint(19) not null primary key,
    category_id  bigint(19) not null,
    brand_id     bigint(19) not null,
    insert_time  datetime   not null default current_timestamp,
    update_time  datetime   not null default current_timestamp on update current_timestamp,

    unique (category_id, brand_id)
) comment = '品牌类目表';

create table if not exists `spec_group` (
    id           bigint(19)   not null primary key,
    category_id  bigint(19)   not null,
    group_name   varchar(128) not null,
    insert_time  datetime     not null default current_timestamp,
    update_time  datetime     not null default current_timestamp on update current_timestamp
) comment = '规格参数分组表';

create table if not exists `spec_param` (
    id          bigint(19)   not null primary key,
    category_id bigint(19)   not null,
    group_id    bigint(19)   not null,
    param_name  varchar(128) not null,
    is_numeric  tinyint(1)   not null comment '是否数字类型参数，0否，1是',
    unit        varchar(32)  default '' comment '数字类型的参数，非数字类型可为空',
    is_generic     tinyint(1)   not null comment '是否为sku通用属性，0否，1是',
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp
) comment = '规格参数表';

create table if not exists `spu` (
    id          bigint(19)   not null primary key,
    spu_name    varchar(256) not null,
    sub_title   varchar(256) default '',
    cid1        bigint(19)   not null,
    cid2        bigint(19)   not null,
    cid3        bigint(19)   not null,
    brand_id    bigint(19)   not null,
    sale_status tinyint(1)   not null default '1' comment '是否上架，0下架，1上架',
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp,

    unique (cid1, cid2, cid3)
) comment = 'SPU';

create table if not exists `spu_detail` (
    id           bigint(19)  not null primary key,
    spu_id       bigint(19)  not null,
    description  text        not null,
    insert_time  datetime    not null default current_timestamp,
    update_time  datetime    not null default current_timestamp on update current_timestamp
) comment = 'SPU详情';


create table if not exists `sku`(
    id             bigint(19)     not null primary key,
    spu_id         bigint(20)     not null,
    title          varchar(256)   not null,
    images         text           not null,
    stock          int(8)         unsigned default '9999',
    origin_price   decimal(10, 2) not null default '0',
    discount_price decimal(10, 2) not null default '0',
    enable         tinyint(1)     not null default '1' comment '是否有效，0无效，1有效',
    generic_spec   json           null comment '通用规格参数数据',
    special_spec   json           null comment '特有规格参数及可选值信息',
    insert_time    datetime       not null default current_timestamp,
    update_time    datetime       not null default current_timestamp on update current_timestamp,

    index (spu_id)
) comment = 'SKU表';
