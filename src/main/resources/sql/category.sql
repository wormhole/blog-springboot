create table blog.category
(
  id         char(36)     not null
    primary key,
  name       varchar(100) not null,
  code       varchar(100) not null,
  deleteable int          not null,
  date       datetime     not null,
  constraint category_name_uindex
  unique (name),
  constraint category_code_uindex
  unique (code)
);


