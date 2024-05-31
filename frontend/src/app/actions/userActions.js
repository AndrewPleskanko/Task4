import axios from 'axios';
import {
    USER_LOADED,
    SET_EDIT_MODE,
    SET_ERROR,
    SET_ERRORS,
    SET_SUCCESS,
    CLEAR_SUCCESS,
    SET_FORM_DATA,
    SET_FILTER,
    SET_PAGE,
    LOAD_SETTINGS,
    SAVE_SETTINGS,
    FETCH_USER,
    FETCH_USER_REQUEST,
    FETCH_USERS_SUCCESS, DELETE_USER,
} from '../constants/userActionTypes';
import {GET_USER, GET_USER_LIST} from "../constants/apiConstants";
import {
    API_BASE_URL,
    ERROR_MESSAGE,
    SUCCESS_MESSAGE_CREATE,
    SUCCESS_MESSAGE_UPDATE
} from "../../pages/user-details/constants/userConstants";
import {ERROR_MESSAGE_DELETE, SUCCESS_MESSAGE_DELETE} from "../../pages/user-list/constants/messageConstants";

export const fetchUsers = (page, filter) => async (dispatch) => {
    dispatch({type: FETCH_USER_REQUEST});
    try {
        const response = await axios.post(`${GET_USER_LIST}`, {page: page - 1, ...filter});
        dispatch({type: FETCH_USERS_SUCCESS, payload: response});
    } catch (error) {
        dispatch({type: SET_ERROR, payload: ERROR_MESSAGE});
    }
};

export const fetchUser = (id) => async (dispatch) => {
    dispatch({type: FETCH_USER_REQUEST});
    try {
        const response = await axios.get(`${API_BASE_URL}/${id}`);
        dispatch({type: FETCH_USER, payload: response});
    } catch (error) {
        dispatch({type: SET_ERROR, payload: ERROR_MESSAGE});
    }
};

export const createUser = (formData) => async (dispatch) => {
    try {
        const response = await axios.post(API_BASE_URL, formData);
        dispatch({type: USER_LOADED, payload: response});
        dispatch({type: SET_SUCCESS, payload: SUCCESS_MESSAGE_CREATE});
        setTimeout(() => dispatch({type: CLEAR_SUCCESS}), 5000);
    } catch (error) {
        dispatch({type: SET_ERROR, payload: ERROR_MESSAGE});
    }
};

export const updateUser = (id, formData) => async (dispatch) => {
    try {
        const response = await axios.put(`${API_BASE_URL}/${id}`, formData);
        dispatch({type: USER_LOADED, payload: response});
        dispatch({type: SET_SUCCESS, payload: SUCCESS_MESSAGE_UPDATE});
        setTimeout(() => dispatch({type: CLEAR_SUCCESS}), 5000);
        dispatch({type: SET_EDIT_MODE, payload: false});
    } catch (error) {
        dispatch({type: SET_ERROR, payload: ERROR_MESSAGE});
    }
};

export const deleteUser = (userId) => async (dispatch) => {
    try {
        await axios.delete(`${GET_USER}${userId}`);
        dispatch({type: DELETE_USER, payload: userId});
        dispatch({type: SET_SUCCESS, payload: SUCCESS_MESSAGE_DELETE});
        setTimeout(() => dispatch({type: CLEAR_SUCCESS}), 20000);
    } catch (error) {
        dispatch({type: SET_ERROR, payload: ERROR_MESSAGE_DELETE});
    }
};

export const setEditMode = (editMode) => (dispatch) => {
    if (typeof editMode !== 'undefined') {
        dispatch({type: SET_EDIT_MODE, payload: editMode});
    }
};

export const setFormData = (formData) => {
    return {type: SET_FORM_DATA, payload: formData};
};

export const setErrors = (errors) => (dispatch) => {
    dispatch({type: SET_ERRORS, payload: errors});
};

export const setFilter = (filter) => {
    return {type: SET_FILTER, payload: filter};
};

export const setPage = (page) => {
    return {type: SET_PAGE, payload: page};
};

export const loadSettings = () => {
    const settings = localStorage.getItem('settings');
    return {
        type: LOAD_SETTINGS,
        payload: settings ? JSON.parse(settings) : {filter: {username: '', email: ''}, page: 1}
    };
};

export const saveSettings = (settings) => {
    localStorage.setItem('settings', JSON.stringify(settings));
    return {type: SAVE_SETTINGS, payload: settings};
};

export const OPEN_MODAL = 'OPEN_MODAL';
export const CLOSE_MODAL = 'CLOSE_MODAL';

export const openModal = (userId) => ({
    type: OPEN_MODAL,
    payload: userId,
});

export const closeModal = () => ({
    type: CLOSE_MODAL,
});