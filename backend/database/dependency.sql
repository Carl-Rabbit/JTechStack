drop table if exists dependency;

create table dependency (
    id int auto_increment,
    repo_id int,

    dep_id int,
    version varchar(30),

    content text,
    jts_timestamp timestamp default now(),

    primary key (id)
);

truncate dependency;
