import {authenticate} from '../../../../../auth/authentication-service';
import {generateUserToken} from '../../../../../auth/security-helpers';

export default function registerUserMutation (_, payload, context) {
  return new Promise(async (resolve, reject) => {
    const id = payload.id;
    const password = payload.password;
    const authResult = authenticate(id, password);
    if (authResult) {
       let token = await generateUserToken(authResult);
       context.user = authResult;
       resolve({ user: authResult, token: token});
    } else {
       reject('invalid credentials');
    }
  });
};

