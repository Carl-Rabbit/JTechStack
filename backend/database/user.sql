create table user (
    id int,
    name varchar(100) comment 'login field in github API return',

    content text,
    jts_timestamp timestamp default now(),

    primary key (id)
);

truncate user;

