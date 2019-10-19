create sequence quizzo.quiz_pk_seq
  increment by 1
  minvalue 1;

create sequence quizzo.quiz_question_pk_seq
  increment by 1
  minvalue 1;

create sequence quizzo.quiz_answer_multichoice_pk_seq
  increment by 1
  minvalue 1;

create table quizzo.quiz(
  id bigint not null default nextval('quizzo.quiz_pk_seq'::regclass),
  title varchar(80) not null,
  description varchar(1024) not null,
  constraint pk_quiz_id primary key (id)
);

create table quizzo.question(
 id bigint not null default nextval('quizzo.quiz_question_pk_seq'::regclass),
 quiz_id bigint not null,
 question_text varchar(1024) not null,
 question_type char(1),
 constraint pk_quiz_question_id primary key (id),
 constraint fk_question_quiz_id foreign key (quiz_id)
   references quizzo.quiz(id) match full on delete cascade on update no action
);

create table quizzo.multichoice_answer(
  id bigint not null default nextval('quizzo.quiz_answer_multichoice_pk_seq'::regclass),
  question_id bigint not null,
  key char(1) not null,
  label varchar(255) not null,
  score integer not null,
  constraint pk_multichoice_answer_pk primary key (id),
  constraint fk_multichoice_question_id foreign key (question_id)
    references quizzo.question(id) match full on delete cascade on update no action
);



