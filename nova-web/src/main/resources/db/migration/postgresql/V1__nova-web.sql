create table if not exists base_param
(
    id            bigserial
        primary key,
    code          varchar,
    name          varchar,
    module_code   varchar,
    value         varchar,
    default_value varchar,
    param_type    varchar,
    embed         varchar,
    description   varchar,
    deleted       smallint,
    create_by     bigint,
    modify_by     bigint,
    create_time   timestamp,
    modify_time   timestamp
);

comment on column base_param.id is '主键';

comment on column base_param.name is '名称';

comment on column base_param.module_code is '模块编码';

comment on column base_param.value is '参数值';

comment on column base_param.default_value is '默认值';

comment on column base_param.param_type is '参数类型';

comment on column base_param.embed is '是否内置参数';

comment on column base_param.description is '描述';

comment on column base_param.deleted is '逻辑删除';

comment on column base_param.create_by is '创建人';

comment on column base_param.modify_by is '最后修改人';

comment on column base_param.create_time is '创建时间';

comment on column base_param.modify_time is '修改时间';

create table if not exists base_dic
(
    id          bigserial
        primary key,
    code        varchar,
    name        varchar,
    dic_type    varchar,
    embed       varchar,
    memo        varchar,
    deleted     smallint,
    create_by   bigint,
    modify_by   bigint,
    create_time timestamp,
    modify_time timestamp
);

comment on table base_dic is '字典表';

comment on column base_dic.id is '主键';

comment on column base_dic.code is '字典编码';

comment on column base_dic.name is '字典名称';

comment on column base_dic.dic_type is '字典类型';

comment on column base_dic.embed is '是否内置字典';

comment on column base_dic.memo is '备注';

comment on column base_dic.deleted is '逻辑删除';

comment on column base_dic.create_by is '创建人';

comment on column base_dic.modify_by is '最后修改人';

comment on column base_dic.create_time is '创建时间';

comment on column base_dic.modify_time is '最后修改时间';

create table if not exists base_dic_item
(
    id          bigserial
        primary key,
    pid         bigint,
    dic_code    varchar,
    value       varchar,
    text        varchar,
    order_no    integer,
    embed       varchar,
    memo        varchar,
    deleted     smallint,
    create_by   bigint,
    modify_by   bigint,
    create_time timestamp,
    modify_time timestamp
);

comment on table base_dic_item is '字典项';

comment on column base_dic_item.id is '主键';

comment on column base_dic_item.pid is '上级ID';

comment on column base_dic_item.dic_code is '字典编码';

comment on column base_dic_item.value is '字典值';

comment on column base_dic_item.text is '字典文字';

comment on column base_dic_item.order_no is '顺序号';

comment on column base_dic_item.embed is '是否内置字典项';

comment on column base_dic_item.memo is '备注';

comment on column base_dic_item.deleted is '逻辑删除';

comment on column base_dic_item.create_by is '创建人';

comment on column base_dic_item.modify_by is '最后修改人';

comment on column base_dic_item.create_time is '创建时间';

comment on column base_dic_item.modify_time is '最后修改时间';

create table if not exists base_menu
(
    id          bigserial
        primary key,
    code        varchar,
    pcode       varchar,
    name        varchar,
    url         varchar,
    icon        varchar,
    embed       varchar,
    open_type   varchar,
    enabled     varchar,
    order_no    integer,
    deleted     smallint,
    create_by   bigint,
    modify_by   bigint,
    create_time timestamp,
    modify_time timestamp
);

comment on table base_menu is '菜单表';

comment on column base_menu.id is '主键';

comment on column base_menu.code is '菜单编码';

comment on column base_menu.pcode is '上级菜单编码';

comment on column base_menu.name is '菜单名称';

comment on column base_menu.url is '菜单地址';

comment on column base_menu.icon is '菜单图表';

comment on column base_menu.embed is '是否内置';

comment on column base_menu.open_type is '菜单打开方式';

comment on column base_menu.enabled is '是否启用';

comment on column base_menu.order_no is '顺序号';

comment on column base_menu.deleted is '逻辑删除';

comment on column base_menu.create_by is '创建人';

comment on column base_menu.modify_by is '最后修改人';

comment on column base_menu.create_time is '创建时间';

comment on column base_menu.modify_time is '最后修改时间';

create table if not exists base_user
(
    user_id              bigserial
            primary key,
    org_id               bigint,
    dept_id              bigint,
    login_name           varchar,
    login_password       varchar,
    user_name            varchar,
    nick_name            varchar,
    gender               varchar,
    birthday             date,
    account_no           varchar,
    email                varchar,
    phone_number         varchar,
    user_avatar          varchar,
    enabled              varchar,
    user_status          varchar,
    last_login_time      timestamp,
    password_modify_time timestamp,
    deleted              smallint,
    create_by            bigint,
    modify_by            bigint,
    create_time          timestamp,
    modify_time          timestamp
);

comment on table base_user is '用户信息';

comment on column base_user.user_id is '主键';

comment on column base_user.org_id is '组织机构ID';

comment on column base_user.dept_id is '部门ID';

comment on column base_user.login_name is '用户登录名';

comment on column base_user.login_password is '登录密码';

comment on column base_user.user_name is '用户姓名';

comment on column base_user.nick_name is '用户昵称';

comment on column base_user.gender is '性别';

comment on column base_user.birthday is '生日';

comment on column base_user.account_no is '工号';

comment on column base_user.email is '邮箱';

comment on column base_user.phone_number is '手机号';

comment on column base_user.user_avatar is '用户头像';

comment on column base_user.enabled is '是否启用';

comment on column base_user.user_status is '用户状态';

comment on column base_user.last_login_time is '最后登录时间';

comment on column base_user.password_modify_time is '密码最后修改时间';

comment on column base_user.deleted is '逻辑删除';

comment on column base_user.create_by is '创建人';

comment on column base_user.modify_by is '最后修改人';

comment on column base_user.create_time is '创建时间';

comment on column base_user.modify_time is '最后修改时间';



