insert into quizzo.quiz(id, title, description)
values (
  nextval('quizzo.quiz_pk_seq'),
  'First Quiz',
  'The first quiz I thought of'
);

insert into quizzo.question(id, quiz_id, question_text, question_type)
values (
  nextval('quizzo.quiz_question_pk_seq'),
  currval('quizzo.quiz_pk_seq'),
  'What is your name?',
  'M'
);

insert into quizzo.multichoice_answer(id, question_id, key, label, score)
values (
  nextval('quizzo.quiz_answer_multichoice_pk_seq'),
  currval('quizzo.quiz_question_pk_seq'),
  'A',
  'Dumbledore',
  0
);

insert into quizzo.multichoice_answer(id, question_id, key, label, score)
values (
  nextval('quizzo.quiz_answer_multichoice_pk_seq'),
  currval('quizzo.quiz_question_pk_seq'),
  'B',
  'Arthur, King of the Britons',
  100
);

insert into quizzo.multichoice_answer(id, question_id, key, label, score)
values (
  nextval('quizzo.quiz_answer_multichoice_pk_seq'),
  currval('quizzo.quiz_question_pk_seq'),
  'C',
  'Jack in the Box',
  0
);

insert into quizzo.question(id, quiz_id, question_text, question_type)
values (
  nextval('quizzo.quiz_question_pk_seq'),
  currval('quizzo.quiz_pk_seq'),
  'What is your new name?',
  'M'
);

insert into quizzo.multichoice_answer(id, question_id, key, label, score)
values (
  nextval('quizzo.quiz_answer_multichoice_pk_seq'),
  currval('quizzo.quiz_question_pk_seq'),
  'A',
  'Altantis',
  0
);

insert into quizzo.multichoice_answer(id, question_id, key, label, score)
values (
  nextval('quizzo.quiz_answer_multichoice_pk_seq'),
  currval('quizzo.quiz_question_pk_seq'),
  'B',
  'Meh the incredible',
  0
);

insert into quizzo.multichoice_answer(id, question_id, key, label, score)
values (
  nextval('quizzo.quiz_answer_multichoice_pk_seq'),
  currval('quizzo.quiz_question_pk_seq'),
  'B',
  'Credible challenge',
  100
);
