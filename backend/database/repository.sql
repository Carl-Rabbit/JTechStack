drop table if exists repository;

create table repository (
    id int,
    name varchar(100),
    full_name varchar(150),
    owner_id int,

    created_at char(20),
    updated_at char(20),
    pushed_at char(20),

    size int,
    stars int,
    forks int,
    open_issues int,
    watchers int,
    license_key varchar(50),

    allow_forking bool,
    is_template bool,

    topics varchar(500),

    java_version varchar(20),
    management varchar(20),

    content text,
    jts_timestamp timestamp default now(),

    primary key (id)
);

truncate repository;
