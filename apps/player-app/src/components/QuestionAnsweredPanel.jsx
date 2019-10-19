import React from 'react';
import {connect} from 'react-redux';
import * as styles from './QuestionAnsweredPanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

function QuestionAnsweredPanel({question, questionScore}) {
    return (
        <div className="jumbotron">
            <h5>Question</h5>
            <h5 className="display-4">{ question.text }</h5>
            <hr/>
            <h5>You answered...</h5>
            <h4 className="display-4">{ questionScore.answerText }</h4>
        </div>
    );
}

function mapStateToProps(state, props) {
    return {
        question: state.currentQuestion || undefined,
        questionScore: state.questionScore || undefined
    }
}

export default connect(mapStateToProps)(QuestionAnsweredPanel);