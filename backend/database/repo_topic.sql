drop table if exists repo_topic;

create table repo_topic (
    id int auto_increment,
    repo_id int,
    topic_str varchar(60),
    primary key (id)
);

alter table repo_topic add index repo_topic_topic_str_idx (topic_str);

truncate repo_topic;
