import * as poolConfig from '../pool-config';
import * as gameRepository from '../game-repository';
import {Quiz, User} from '../../graphql/generated/graphql';

describe('Game repository', () => {

  // TODO - add another test quiz
  it('should fetch all quizzes and unpack correctly', async (done) => {
    const quizDataList = [
      { quiz_id: 69,	quiz_title: "First Quiz",	quiz_description: "The first quiz I thought of",	question_id: 3, question_text: "What is your name?", answer_id: 1, answer_key: "A", answer_label: "Dumbledore", score: 0 },
      { quiz_id: 69,	quiz_title: "First Quiz",	quiz_description: "The first quiz I thought of",	question_id: 3, question_text: "What is your name?", answer_id: 2, answer_key: "B", answer_label: "Arthur, King of the Britons",	score: 100 },
      { quiz_id: 69,	quiz_title: "First Quiz",	quiz_description: "The first quiz I thought of",	question_id: 3, question_text: "What is your name?", answer_id: 3, answer_key: "C", answer_label: "Jack in the Box",	score: 0 },
      { quiz_id: 69,	quiz_title: "First Quiz",	quiz_description: "The first quiz I thought of",	question_id: 4, question_text: "What is your new name?", answer_id: 4, answer_key: "A", answer_label: "Altantis",	score: 0 },
      { quiz_id: 69,	quiz_title: "First Quiz",	quiz_description: "The first quiz I thought of",	question_id: 4, question_text: "What is your new name?", answer_id: 5, answer_key: "B", answer_label: "Meh the incredible", score: 0 },
      { quiz_id: 69,	quiz_title: "First Quiz",	quiz_description: "The first quiz I thought of",	question_id: 4, question_text: "What is your new name?", answer_id: 6, answer_key: "C", answer_label: "Credible challenge", score: 100 }
    ];

    spyOn(poolConfig, 'getPool').and.returnValue({
      query: function(sql) {
        return Promise.resolve({ rows: quizDataList });
      }
    });

    const result: Quiz[] = await gameRepository.getQuizData();

    console.dir(result);
    // TODO - deeper test
    expect(result[0].questions.length).toBe(2);
    expect(result[0].questions[0]['answers'].length).toBe(3);
    done();
  });
});

it('should load users properly', async (done) => {

  const userDataList = [
    { id: 1, user_name: "krimple", password: "admin123", role_name: "admin" },
    { id: 1, user_name: "krimple", password: "admin123", role_name: "moderator" }
  ];
  spyOn(poolConfig, 'getPool').and.returnValue({
      query: function(sql) {
        return Promise.resolve(userDataList);
      }
    });

  const result: User[] = await gameRepository.getUsersData();

  expect(result.length).toBe(1);
  expect(result).toEqual([
    {

      id: 'krimple',
      password: 'admin123',
      roles: ['admin', 'moderator']
    }
  ]);
  done();
});

