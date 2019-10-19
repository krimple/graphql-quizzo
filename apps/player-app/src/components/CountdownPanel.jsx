import React, {Fragment} from 'react';

export default ({timeLeft}) => {
   return (
       <Fragment>
          {
             timeLeft > 0 &&
             <p>Time left:  {timeLeft} seconds...</p>
          }
          {
            (typeof timeLeft !== 'number' || timeLeft === 0) &&
             <p> Time's up!</p>
          }
       </Fragment>
   );
}
