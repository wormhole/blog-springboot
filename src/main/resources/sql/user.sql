create table blog.user
(
  id         char(36)      not null
    primary key,
  email      varchar(100)  not null,
  password   char(32)  not null,
  nickname   varchar(100)  not null,
  salt       char(32)      not null,
  deleteable int           not null,
  constraint user_email_uindex
  unique (email),
  constraint user_nickname_uindex
  unique (nickname)
);


