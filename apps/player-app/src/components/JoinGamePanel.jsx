import React, { useState } from 'react';
import { joinGameMutation } from '../graphql';
import * as styles from './JoinGamePanel.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);


export default function() {
    const [gameJoined, setGameJoined] = useState(false);
    const registerForGame = async () => {
        try {
            await joinGameMutation();
            setGameJoined(true);
        } catch (e) {
            console.error(e);
            alert('Could not join game.');
        }
    };

    return (
        <div className="jumbotron vertical-center">
            {
                !gameJoined &&
                <div className="container text-center">
                        <button
                            className="btn btn-large btn-outline-primary"
                            onClick={registerForGame}>
                            Click to Play Game
                        </button>
                </div>
            }


            {
                !!gameJoined &&
                <h5 className="container text-center">
                    Please wait for registration to finish...
                </h5>
            }
        </div>
    );
}
