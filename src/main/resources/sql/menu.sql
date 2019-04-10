create table blog.menu
(
  id         char(36)     not null
    primary key,
  name       varchar(100) not null,
  url        varchar(100) not null,
  deleteable int          not null,
  date       datetime     not null
);

