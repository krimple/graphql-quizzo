create sequence quizzo.user_pk_seq
  increment by 1
  minvalue 1;

create sequence quizzo.role_pk_seq
  increment by 1
  minvalue 1;

CREATE TABLE quizzo.user (
  id bigint not null default nextval('quizzo.user_pk_seq'),
  user_name varchar(30) not null,
  password varchar(30) not null,
  constraint pk_user primary key (id)
);

CREATE TABLE quizzo.role (
  id bigint not null default nextval('quizzo.role_pk_seq'),
  role_name varchar not null,
  description varchar not null,
  constraint pk_role primary key (id)
);

CREATE TABLE quizzo.user_role (
  user_id bigint not null,
  role_id bigint not null,
  constraint fk_user_role_user_id foreign key (user_id)
    references quizzo.user(id) match full,
  constraint fk_user_role_role_id foreign key (role_id)
    references quizzo.role(id) match full
);


insert into quizzo.user(user_name, password) values ('krimple', 'admin123');
insert into quizzo.role(role_name, description) values ('admin', 'Administrator');

insert into quizzo.user_role(user_id, role_id)
values (currval('quizzo.user_pk_seq'), currval('quizzo.role_pk_seq'));

insert into quizzo.role(role_name, description) values ('moderator', 'Moderator');

insert into quizzo.user_role(user_id, role_id)
values (currval('quizzo.user_pk_seq'), currval('quizzo.role_pk_seq'));



