drop table if exists contributor;

create table contributor (
    id int auto_increment,
    repo_id int,

    user_id int,
    contributions int,

    content text,
    jts_timestamp timestamp default now(),

    primary key (id)
);

truncate contributor;
