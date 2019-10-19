import { getPool } from './pool-config';
import { uniqBy }from 'lodash';
import logger from '../server/logger'
import {ChoiceOption, Question, Quiz, User} from '../graphql/generated/graphql';

// TODO - refactor, NOT SUPER EFFICIENT BUT WORKS ATM and avoids multi-level SQL queries
const QUERY_QUIZ_DATA = `
select quiz.id as quiz_id, quiz.title as quiz_title, quiz.description as quiz_description,
  question.id as question_id, question.question_text as question_text,
  answer.id as answer_id, answer.key as answer_key, answer.label as answer_label,
  answer.score as score
from
quizzo.quiz as quiz
inner join quizzo.question as question on (question.quiz_id = quiz.id)
inner join quizzo.multichoice_answer as answer on (answer.question_id = question.id)
order by question.id, answer.key
`;

const QUERY_USERS_AND_ROLES = `
select u.id, user_name, password, r.role_name
from quizzo.user u
  inner join quizzo.user_role ur on (ur.user_id = u.id)
  inner join quizzo.role r on (ur.role_id = r.id)
order by u.id
`;

export function getQuizData(): Promise<Quiz[]> {
  return new Promise(async (resolve, reject) => {
    try {
      let queryResult = await getPool().query(QUERY_QUIZ_DATA);
      // start as amorphous blob...  b/c the type has required values
      // note - yuck, but downside of interfaces for GraphQL mappings
      // maybe research mapping db layer to GraphQL LAYER.

      if (!queryResult || !queryResult.rows) {
        reject(`No quiz data for quiz$ {quizId}`);
        return;
      }

      // pg result in 'rows' prop
      const quizDataList = queryResult.rows;

      console.dir(quizDataList);
      const quizzes = uniqBy(quizDataList, 'quiz_id').map(quiz => ({
        quiz_id: quiz.quiz_id, title: quiz.quiz_title, description: quiz.quiz_description
      }));


      const quizzesWithQuestions = quizzes.map(quiz => ({
        ...quiz,
        questions: uniqBy(quizDataList.filter(q => q.quiz_id === quiz.quiz_id), 'question_id')
          .map(question => ({
            question_id: question.question_id,
            question_text: question.question_text
          }))
      }));

      const quizzesWithQandA = quizzesWithQuestions.map(quiz => ({
        ...quiz,
        questions: quiz.questions.map(question => ({
          ...question,
          answers: quizDataList
            .filter(q => q.quiz_id === quiz.quiz_id &&
                    q.question_id === question.question_id)
            .map(answer => ({
              answer_id: answer.answer_id,
              key: answer.answer_key,
              label: answer.answer_label,
              score: answer.score
            }))
        }))
      }));

      console.log(`********* RESULTS RETURNED`);
      console.dir(quizzesWithQandA);

      // TODO - not in love with this
      resolve(quizzesWithQandA as Quiz[]);
    } catch (e) {
      reject(e);
    }
  });
}

export function getUsersData(): Promise<User[]> {
  return new Promise(async(resolve, reject) => {
    try {
      let queryResult = await getPool().query(QUERY_USERS_AND_ROLES);
      if (!queryResult || queryResult.rows === 0) {
        reject('No users found. Please reset the system and try again.');
        return;
      }

      // pg stores results in 'rows'
      const users = queryResult.rows;

      const usersWithRoles = users.reduce(
          (users, userwithrole) => {
            let user = users.find(u => u.user_name === users.id);
            if (!user) {
              user = {
                id: userwithrole.user_name,
                password: userwithrole.password,
                roles: [userwithrole.role_name]
              };
              users.push(user);
            } else {
              user.roles.push(userwithrole.role_name)
            }

            return users;
          }, []) as User[];

      resolve(usersWithRoles);
    }
    catch (e) {
      reject(e);
    }
  });
}
