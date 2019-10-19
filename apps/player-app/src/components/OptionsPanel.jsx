import React, {Component} from 'react';
import {answerQuestionMutation} from '../graphql/answer-question-mutation';
import eq from 'lodash/eq';
import QuizButton from './QuizButton';
import * as styles from './OptionsPanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

export default class OptionsPanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            answered: false
        };
    }

  doAnswerQuestion = async (key) => {
      try {
          await answerQuestionMutation(key);
      } catch (e) {
          alert('Answer call failed. See console.');
          console.log(e);
      }
  };

  shouldComponentUpdate(nextProps, nextState, nextContext) {
      if (!this.props.answered || !nextProps.answered || this.props.answered !== nextProps.answered) {
          return true;
      }

      if (!this.props.choiceOptions && !nextProps.choiceOptions) {
          console.log('both are empty, do not render');
          return false;
      }

      // if we are getting new options or clearing ours out, update
      if (this.props.choiceOptions && !nextProps.choiceOptions) {
          console.log('rerender options we are removing options');
          return true;
      }
      if (!this.props.choiceOptions && nextProps.choiceOptions ) {
          console.log('rerender options we have next options');
          return true;
      }

      // if we have both and they differ, render
      const propsAreDifferent = !eq(this.props.choiceOptions, nextProps.choiceOptions);
      if (propsAreDifferent) {
          console.log('rerender options via eq');
      }
      return propsAreDifferent;
  }

  render() {
      if (!this.props || !this.props.choiceOptions) {
          return '';
      } else if (this.props.answered) {
         return (
             <div className="jumbotron">
                 <h5>Answer submitted...</h5>
             </div>
         );
      } else {
          return <div className="jumbotron">
              {
                  this.props.choiceOptions &&
                  this.props.choiceOptions.map(
                      (choiceOption) => (
                          <QuizButton key={choiceOption.key}
                                      enabled={ !this.state.answered }
                                      onClick={(e) => {
                                          this.doAnswerQuestion(choiceOption.key);
                                      }}
                                      buttonText={choiceOption.key} label={choiceOption.label}>
                          </QuizButton>
                  ))
              }
          </div>;
      }
  }
}