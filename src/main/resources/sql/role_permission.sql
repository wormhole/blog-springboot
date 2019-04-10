create table blog.role_permission
(
  id           char(36) not null
    primary key,
  roleid       char(36) not null,
  permissionid char(36) not null
);


