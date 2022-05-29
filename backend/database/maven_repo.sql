drop table if exists maven_repo;

create table maven_repo (
    id int auto_increment,

    name varchar(50),
    group_id varchar(50),
    artifact_id varchar(50),
    description varchar(200),
    categories varchar(50),
    tags varchar(100),
    used_by int,

    version_usages text,

    jts_timestamp timestamp default now(),

    primary key (id)
);

truncate maven_repo;
