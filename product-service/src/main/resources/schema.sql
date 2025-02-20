create database if not exists `product-service`;
use `product-service`;
create table if not exists `category`
(
    id             bigint(19)   not null primary key,
    name           varchar(128) not null,
    image_url      mediumtext   null comment '类目图片',
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
    name        varchar(128) not null,
    category_id bigint(19)   not null,
    status      tinyint(1)   not null default 0 comment '是否使用，0否，1是',
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp
) comment = '品牌表';

create table if not exists `brand_snapshot` (
    id          bigint(19)   not null primary key,
    name        varchar(128) not null,
    user_id     bigint(19)  not null,
    category_id bigint(19)   not null,
    status      tinyint(1)   not null default 0 comment '审核状态，0审核中，1审核通过，2审核拒绝',
    reason      varchar(255) null,
    insert_time datetime     not null default current_timestamp,
    update_time datetime     not null default current_timestamp on update current_timestamp
) comment = '品牌快照表';

create table if not exists `attribute` (
    id          bigint(19)    not null primary key,
    category_id bigint(19)    not null,
    name        varchar(128)  not null,
    is_numeric  tinyint(1)    not null default 0  comment '是否数字类型参数，0否，1是',
    unit        varchar(32)   default '' comment '数字类型的参数，非数字类型可为空',
    is_generic  tinyint(1)    not null default  1 comment '是否为sku通用属性，0否，1是',
    value       varchar(1000) null,
    insert_time datetime      not null default current_timestamp,
    update_time datetime      not null default current_timestamp on update current_timestamp,

    unique(category_id, name)
) comment = '规格表';

create table if not exists `spu` (
    id               bigint(19)   not null primary key,
    name             varchar(256) not null,
    category_id      bigint(19)   not null,
    brand_id         bigint(19)   null,
    image_url        mediumtext   null comment 'SKU图片',
    sale_status      tinyint(1)   not null default '1' comment '是否上架，0下架，1上架',
    tags             json         null comment '产品标签',
    generic_spec     json         null comment '通用规格参数数据',
    special_spec     json         null comment '特有规格参数及可选值信息',
    insert_time      datetime     not null default current_timestamp,
    update_time      datetime     not null default current_timestamp on update current_timestamp,

    unique (name, category_id)
) comment = 'SPU';

create table if not exists `spu_user` (
    id           bigint(19) not null primary key,
    user_id      bigint(19) not null,
    spu_id       bigint(19) not null,
    insert_time  datetime   not null default current_timestamp,
    update_time  datetime   not null default current_timestamp on update current_timestamp
) comment = 'SPU、用户关联表';

create table if not exists `sku`(
    id               bigint(19)     not null primary key,
    spu_id           bigint(19)     not null,
    name             varchar(256)   not null,
    image_url        mediumtext     not null comment '产品主图',
    detail_image_url mediumtext     not null comment '产品详情图',
    stock            int(8)         unsigned default '9999',
    origin_price     decimal(10, 2) not null default '0',
    discount_price   decimal(10, 2) not null default '0',
    unit             varchar(65)    not null,
    description      text           null,
    enable           tinyint(1)     not null default '1' comment '是否有效，0无效，1有效',
    insert_time      datetime       not null default current_timestamp,
    update_time      datetime       not null default current_timestamp on update current_timestamp,

    unique(spu_id, name),
    index (spu_id)
) comment = 'SKU表';

create table if not exists `sku_user` (
    id           bigint(19) not null primary key,
    user_id      bigint(19) not null,
    sku_id       bigint(19) not null,
    insert_time  datetime   not null default current_timestamp,
    update_time  datetime   not null default current_timestamp on update current_timestamp
) comment = 'SKU、用户关联表';

create table if not exists `rdc_spu` (
  id          bigint(19) not null primary key ,
  rdc_id      bigint(19) not null,
  spu_id      bigint(19) not null,
  insert_time datetime   not null default current_timestamp,
  update_time datetime   not null default current_timestamp on update current_timestamp,

  unique(spu_id, rdc_id)
) comment = 'RDC、SPU关联表';
