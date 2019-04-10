create table blog.visitor
(
  id      char(36)        not null
    primary key,
  ip      varchar(100)    not null,
  agent   varchar(300),
  date    datetime        not null
);