import React, { Fragment } from 'react';
import * as styles from './ReportScorePanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

export default function(props) {
    let report;

    if (props.questionScore) {}
    return(
     <div className="jumbotron">
        <h5>Question</h5>
        <h5 className="display-4">{ props.question.text }</h5>
        <hr/>
         {
             !!props.questionScore &&
             <Fragment>
                 <h5>You answered...</h5>
                 <h4 className="display-4">{ props.questionScore.answerText }</h4>
                 <h5>Your answer was...</h5>
                 {
                     props.questionScore.score > 0 &&
                     <h4 className="display-4">Correct!</h4>
                 }
                 {
                     !props.questionScore.score &&
                     <h4 className="display-4">Incorrect...</h4>
                 }
             </Fragment>

         }

         {
             !props.questionScore &&
                 <Fragment>
                     <h4 className="display-4">No answer...</h4>
                 </Fragment>
         }

     </div>);
}