import React from 'react';
import QuestionPanel from './QuestionPanel';
import CountdownPanel from './CountdownPanel';
import OptionsPanel from "./OptionsPanel";
import * as styles from './GamePlayPanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);


export default function({question, answered, choiceOptions, timeLeft}) {
    console.log(Date.now(), 'rendering gameplay panel');
    console.log(question, choiceOptions, timeLeft);
  return (
      <div className="jumbotron" id="GamePlayPanel">
        {
          !!question &&
          <QuestionPanel question={question} />
        }

        {
          !!choiceOptions &&
          <OptionsPanel answered={answered} choiceOptions={choiceOptions} />
        }

        {
          !!timeLeft &&
          <CountdownPanel timeLeft={timeLeft} />
        }

      </div>
  );
}

