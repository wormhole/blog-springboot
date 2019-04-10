create table blog.role
(
  id       char(36)     not null
    primary key,
  name varchar(100) not null,
  code varchar(100) not null,
  constraint role_name_uindex
  unique (name),
  constraint role_code_uindex
  unique (code)
);


