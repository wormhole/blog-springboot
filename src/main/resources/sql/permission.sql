create table blog.permission
(
  id             char(36)     not null
    primary key,
  name varchar(100) not null,
  code varchar(100) not null,
  constraint permission_name_uindex
  unique (name),
  constraint permission_code_uindex
  unique (code)
);


