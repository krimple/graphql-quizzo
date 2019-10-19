import React from 'react';
import styles from './QuizButton.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

export default (props) => {

  return (
      <div className="row mb-4">
         <button className="btn btn-large btn-outline-primary btn-block"
                  onClick={ (e) => { props.onClick(e); }}>
           {props.label}
         </button>
      </div>
  );
};
