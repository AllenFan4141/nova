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
    anon_access   varchar,
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

comment on column base_param.anon_access is '是否可以匿名访问';

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

create table if not exists base_org
(
    org_id        bigserial
        primary key,
    org_pid       bigint,
    org_name      varchar,
    org_code      varchar,
    org_full_name varchar,
    enabled       varchar,
    order_no      varchar,
    deleted        integer,
    create_by     bigint,
    modify_by     bigint,
    create_time   timestamp,
    modify_time   timestamp
);

comment on table base_org is '组织机构';

comment on column base_org.org_id is '主键';

comment on column base_org.org_pid is '上级组织ID';

comment on column base_org.org_name is '组织机构名称';

comment on column base_org.org_code is '组织机构编码';

comment on column base_org.org_full_name is '组织机构全称';

comment on column base_org.enabled is '启用';

comment on column base_org.order_no is '顺序号';

comment on column base_org.deleted is '逻辑删除';

comment on column base_org.create_by is '创建人';

comment on column base_org.modify_by is '最后修改人';

comment on column base_org.create_time is '创建时间';

comment on column base_org.modify_time is '最后修改时间';

create table if not exists base_dept
(
    dept_id        bigserial
        primary key ,
    dept_pid       bigint,
    org_id         bigint,
    dept_name      varchar,
    dept_code      varchar,
    dept_full_name varchar,
    enabled        varchar,
    order_no       varchar,
    deleted         integer,
    create_by      bigint,
    modify_by      bigint,
    create_time    timestamp(6),
    modify_time    timestamp(6)

);

comment on table base_dept is '组织机构';

comment on column base_dept.dept_id is '主键';

comment on column base_dept.dept_pid is '上级部门ID';

comment on column base_dept.org_id is '组织机构ID';

comment on column base_dept.dept_name is '部门名称';

comment on column base_dept.dept_code is '部门编码';

comment on column base_dept.dept_full_name is '部门全称';

comment on column base_dept.enabled is '启用';

comment on column base_dept.order_no is '顺序号';

comment on column base_dept.deleted is '逻辑删除';

comment on column base_dept.create_by is '创建人';

comment on column base_dept.modify_by is '最后修改人';

comment on column base_dept.create_time is '创建时间';

comment on column base_dept.modify_time is '最后修改时间';



create table if not exists base_opt_log
(
    log_id          bigserial
            primary key,
    log_title       varchar,
    log_url         varchar,
    http_method     varchar,
    java_method     integer,
    opt_type        varchar,
    opt_time        timestamp,
    opt_ip          varchar,
    time_cost       bigint,
    success         varchar,
    response        varchar,
    request         varchar,
    opt_description varchar,
    operator_id     bigint,
    operator_name   varchar,
    deleted         integer,
    create_by       bigint,
    modify_by       bigint,
    create_time     timestamp,
    modify_time     timestamp

);



comment on table base_opt_log is '操作日志';

comment on column base_opt_log.log_id is '主键';

comment on column base_opt_log.log_title is '操作标题';

comment on column base_opt_log.log_url is '日志请求地址';

comment on column base_opt_log.http_method is '请求方式';

comment on column base_opt_log.java_method is 'java方法调用';

comment on column base_opt_log.opt_type is '操作类型';

comment on column base_opt_log.opt_time is '操作时间';

comment on column base_opt_log.opt_ip is '操作的IP地址';

comment on column base_opt_log.time_cost is '操作耗时';

comment on column base_opt_log.success is '响应是否成功';

comment on column base_opt_log.response is '响应结果';

comment on column base_opt_log.request is '请求内容';

comment on column base_opt_log.opt_description is '操作的文字描述,使用SpEL表达式解析后的文字内容';

comment on column base_opt_log.operator_id is '操作人ID';

comment on column base_opt_log.operator_name is '操作人姓名';

comment on column base_opt_log.deleted is '逻辑删除';

comment on column base_opt_log.create_by is '创建人';

comment on column base_opt_log.modify_by is '最后修改人';

comment on column base_opt_log.create_time is '创建时间';

comment on column base_opt_log.modify_time is '修改时间';







