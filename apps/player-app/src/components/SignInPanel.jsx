import React, {Component, Fragment} from 'react';
import {connect} from 'react-redux';
import * as actionCreators from '../store/action-creators';
import * as styles from './SignIn.module.scss';
import classNames from 'classnames';
const cx = classNames.bind(styles);

class SignInPanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            nickName: '',
            error: undefined
        };
    }

    handleSubmit  = async (event) => {
        event.preventDefault();
        // TODO - do we really even care here about awaiting? connect will provide feedback
        try {
            await this.props.dispatch(actionCreators.signOnToQuizzoSystem(this.state.nickName));
            // begin that ole' polling magic only you do...
            this.props.dispatch(actionCreators.beginPollingStatus());
        } catch (e) {
            console.log(`Error occurred during connection... ${JSON.stringify(e, null, 2)}`);
        }
    };


    render() {
        return (
            <div className="container">
                <div className="row h-100 justify-content-center align-items-center">
                    <header className="align-content-center align-middle">
                        <h1 className="text-center">Join the game!</h1>
                    </header>
                    {
                        this.props.signOnError &&
                        <div>{JSON.stringify(this.props.signOnError)}</div>
                    }
                    <form className="col-12 my-auto form-inline justify-content-center" onSubmit={this.handleSubmit}>
                        <label className="sr-only" htmlFor="nickNameInput">Nickname</label>

                        <input value={this.state.nickName}
                               className={cx(styles.signInSized, 'form-control', 'form-control-lg', 'mb-4', 'mr-sm-4')}
                               placeholder="Enter your Nickname"
                               onChange={event => {
                                   this.setState({
                                       nickName: event.target.value
                                   })
                               }}
                               type="text"
                               name="nickName"
                               required
                        />
                        <button
                            className="btn btn-lg btn-outline-primary mb-4"
                            onClick={this.handleSubmit}
                            type="submit">
                            Go!
                        </button>
                    </form>
                </div>
            </div>
        );
    }
}

function mapStateToProps(state, props) {
    return {
        nickName: state.nickName || undefined,
        signOnInProgress: state.signOnInProgress,
        signOnError: state.signOnError
    };
}

export default connect(mapStateToProps)(SignInPanel);
