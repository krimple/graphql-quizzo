export default {
    Question: {
        __resolveType(obj, context, info) {
            if (obj.options) {
                return 'MultipleChoiceQuestion';
            }
            if (obj.correct !== undefined) {
                return 'TrueFalseQuestion';
            }
            if (obj.correctText) {
                return 'FillInBlankQuestion';
            }
            return null;
        }
    },
    QuestionData: {
        __resolveType(obj, context, info) {
            return 'Question';
        }
    },
    GamePlayMessage: {
        __resolveType(obj, context, info) {
            if (obj.gameToken) {
                return 'GameStart';
            }
            if (obj.question) {
                return 'SubmitQuestion';
            }
            if (obj.yourAnswer) {
                return 'TurnOver';
            }
            if (obj.finalScores) {
                return 'GameOver';
            }
            if (typeof obj.timeLeft === 'number') {
                return 'TickTock';
            }
        }
    }
}
