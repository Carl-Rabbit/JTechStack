drop table if exists maven_repo;

create table maven_repo (
    id int auto_increment,

    name varchar(50),
    group_id varchar(50),
    artifact_id varchar(50),
    description varchar(200),
    license varchar(50),
    img_url varchar(100),
    categories varchar(100),
    tags varchar(100),
    used_by int,
    versions text comment 'list of {version: string, vuln: int, usages: int, date: string}',

    jts_timestamp timestamp default now(),

    primary key (id)
);

alter table maven_repo add unique index (group_id, artifact_id);

truncate maven_repo;
