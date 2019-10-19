import React, {Component, Fragment} from 'react';
import SignInPanel from './SignInPanel';
import GamePlayPanel from './GamePlayPanel';
import JoinGamePanel from "./JoinGamePanel";
import QuestionAnsweredPanel from "./QuestionAnsweredPanel";
import ReportScorePanel from "./ReportScorePanel";
import {connect} from 'react-redux';
import {beginPollingStatus, assertMyCredentials} from '../store/action-creators';
import * as styles from './MainPanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

class MainPanel extends Component {

    constructor(props) {
        super(props);
        this.state = {
            authenticated: false
        }
    }

    async componentDidMount() {
        if (!this.props.authenticated) {
            await this.props.dispatch(assertMyCredentials());
            await this.props.dispatch(beginPollingStatus());
        }
    }

    render() {
        const {gameMode, nickName, currentQuestion, currentOptions, questionScore, finalScore, ...props} = this.props;

        if (!this.props.nickName) {
            return (
                <Fragment>
                    <SignInPanel />
                </Fragment>
            );
        }

        return <div className="container">
            {
                (gameMode === 'NOT_RUNNING' || gameMode === 'IDLE' || gameMode === 'NOT_FOUND') &&
                <h3 className="h-100 text-center align-content-center">Awaiting next game...</h3>
            }

            {
                gameMode === 'AWAITING_PLAYERS' &&
                <JoinGamePanel/>
            }

            {
                gameMode === 'PRESENTING_QUESTION' && !!currentQuestion && !!currentOptions && !questionScore &&
                <GamePlayPanel
                    question={currentQuestion}
                    choiceOptions={currentOptions}/>
            }

            {
                gameMode === 'PRESENTING_QUESTION' && questionScore &&
                <QuestionAnsweredPanel question={currentQuestion} questionScore={questionScore}/>
            }

            {
                gameMode === 'PRESENTING_SCORES' &&
                <ReportScorePanel
                    questionScore={questionScore}
                    question={currentQuestion}
                    choiceOptions={currentOptions}/>
            }

            {
                gameMode === 'GAME_OVER' &&
                <Fragment>
                    <h3 className='display-3 text-center'>Game over. Score:</h3>
                    <h3 className='display-3 text-center'>{finalScore}</h3>
                </Fragment>
            }
        </div>;
    }
}

function mapStateToProps(state, props) {
    return {
        authenticated: state.authenticated || false,
        nickName: state.nickName || undefined,
        gameMode: state.gameMode || undefined,
        currentQuestion: state.currentQuestion || undefined,
        currentOptions: state.currentOptions || undefined,
        questionScore: state.questionScore || undefined,
        finalScore: state.finalScore || 0
    }
}
export default connect(mapStateToProps)(MainPanel);
