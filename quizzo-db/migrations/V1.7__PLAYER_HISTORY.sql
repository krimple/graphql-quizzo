create sequence quizzo.player_history_seq
  increment by 1
  minvalue 1;

CREATE TABLE quizzo.player_history (
  id bigint not null default nextval('quizzo.player_history_seq'),
  answer_date date not null,
  quiz_name varchar not null,
  player_nickname varchar not null,
  question_text varchar not null,
  answer_value varchar not null,
  answer_text varchar not null,
  answer_score int not null,
  total_score int not null,
  final int default 0
);
