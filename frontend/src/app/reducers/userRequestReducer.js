import {
    CREATE_USER,
    DELETE_USER, FETCH_USER, FETCH_USER_REQUEST,
    FETCH_USERS_SUCCESS, LOAD_SETTINGS,
    LOAD_USERS, SAVE_SETTINGS, SET_EDIT_MODE,
    SET_FILTER,
    SET_FORM_DATA,
    SET_PAGE, UPDATE_USER
} from "../constants/userActionTypes";

const initialState = {
    users: [],
    userRequest: null,
    loading: false,
    editMode: false,
    error: null,
    errors: {},
    success: null,
    filter: {username: '', email: ''},
    page: 1,
};

const userRequestReducer = (state = initialState, action) => {
    console.log('Dispatching ', action.type);
    switch (action.type) {
        case FETCH_USER_REQUEST:
            return {
                ...state,
                loading: true,
            };
        case FETCH_USER:
            return {
                ...state,
                userRequest: action.payload,
                editMode: false,
                loading: false,
                error: null,
                success: null
            };
        case CREATE_USER:
            return {
                ...state,
                userRequest: action.payload,
                editMode: false,
                error: null,
                success: 'User created successfully'
            };
        case UPDATE_USER:
            return {
                ...state,
                userRequest: action.payload,
                editMode: false,
                error: null,
                success: 'User updated successfully'
            };
        case SET_EDIT_MODE:
            console.log('Setting edit mode to:', action.payload);
            return {
                ...state,
                editMode: action.payload
            };
        case SET_FORM_DATA:
            return {
                ...state,
                formData: action.payload
            };
        case FETCH_USERS_SUCCESS:
            return {
                ...state,
                users: action.payload.content,
                totalPages: action.payload.totalPages,
                loading: false,
                error: null
            };
        case LOAD_SETTINGS:
            return {
                ...state,
                ...action.payload
            };
        case SAVE_SETTINGS:
            return {
                ...state,
                ...action.payload
            };
        case LOAD_USERS:
            return {...state, users: action.payload, loading: false, error: null};
        case DELETE_USER:
            return {...state, users: state.users.filter(user => user.id !== action.payload), error: null};
        case SET_FILTER:
            return {...state, filter: action.payload};
        case SET_PAGE:
            return {...state, page: action.payload};
        default:
            return state;
    }
};

export default userRequestReducer;