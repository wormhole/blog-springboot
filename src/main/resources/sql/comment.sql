create table blog.comment
(
  id        char(36)     not null
    primary key,
  nickname  varchar(100) not null,
  email     varchar(100) not null,
  website   varchar(100) null,
  content   text         not null,
  articleid char(36)     not null,
  date      datetime     not null,
  replyto   varchar(100) null,
  review    int          not null
);

