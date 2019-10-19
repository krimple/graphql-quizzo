import React, {Fragment } from 'react';
import {connect} from 'react-redux';
import * as styles from './QuestionPanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

function QuestionPanel({question}) {
  return(
      <Fragment>
        {
          !!question &&
              <div className="row">
                <div className="col-12"><h5>{ question.text }</h5></div>
              </div>
        }
        </Fragment>
  );
}

function mapStateToProps(state, props) {
  return {
    question: state.currentQuestion || null
  }
}
export default connect(mapStateToProps)(QuestionPanel);
