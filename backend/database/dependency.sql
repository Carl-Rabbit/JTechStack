drop table if exists dependency;

create table dependency (
    id int auto_increment,
    repo_id int,

    mvn_repo_id varchar(100),
    version varchar(30),

    content text,
    jts_timestamp timestamp default now(),

    primary key (id)
);

alter table dependency add index dependency_repo_id_idx (repo_id);

alter table dependency add index dependency_mvn_repo_id_idx (mvn_repo_id);

truncate dependency;
