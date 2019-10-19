import startRegistrationsMutation from './start-registrations-mutation';
import nextQuestionMutation from './next-question-mutation';
import startGamePlayMutation from './start-gameplay-mutation';
import endTurnMutation from './end-turn-mutation';

export default  {
  startRegistrations: startRegistrationsMutation,
  startGamePlay: startGamePlayMutation,
  endTurn: endTurnMutation,
  nextQuestion: nextQuestionMutation
};
