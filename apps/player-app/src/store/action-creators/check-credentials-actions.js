import * as actions from '../actions';
import {meQuery} from "../../graphql";

export function assertMyCredentials() {
    return async (dispatch) => {
        try {
            const answer = await meQuery();
            if (answer.data.me !== 'anonymousUser') {
                dispatch({
                    type: actions.ACTION_ASSERT_JWT_TOKEN_EXISTS,
                    nickName: answer.data.me
                });
            } else {
                dispatch({
                    type: actions.ACTION_ASSERT_JWT_TOKEN_NOT_FOUND
                });
            }
        } catch(e) {
            // TODO - more strong than this?
            console.error(e);
            dispatch({
                type: actions.ACTION_ASSERT_JWT_TOKEN_NOT_FOUND
            });
        }
    }

}