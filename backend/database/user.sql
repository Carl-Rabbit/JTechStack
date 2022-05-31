drop table if exists user;

create table user (
    id int,
    login varchar(100),

    content text,
    jts_timestamp timestamp default now(),

    primary key (id)
);

truncate user;

