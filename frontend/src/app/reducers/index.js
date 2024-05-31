import { combineReducers } from 'redux';

import userRequest from './userRequestReducer';
import user from './user';

export default combineReducers({
  userRequest,
  user,

});