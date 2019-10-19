alter table quizzo.player_history
add column quiz_id bigint;

alter table quizzo.player_history add
constraint fk_player_history_quiz_id foreign key (quiz_id)
   references quizzo.quiz(id) match full on delete cascade on update no action;

alter table quizzo.player_history
  add column question_id int;
