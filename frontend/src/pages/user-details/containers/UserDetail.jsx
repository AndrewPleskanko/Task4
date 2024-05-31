import {useDispatch, useSelector} from 'react-redux';
import {useNavigate, useParams} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faEdit} from '@fortawesome/free-solid-svg-icons';
import {validate} from '../validation/userValidation';
import {
    AGE_LABEL, BACK_BUTTON_TEXT, CANCEL_BUTTON_TEXT,
    CREATE_BUTTON_TEXT, EMAIL_LABEL, LOADING_USER_DATA_TEXT,
    NO_AGE_SET_TEXT, NO_PASSWORD_SET_TEXT,
    NO_PHONE_NUMBER_SET_TEXT, PASSWORD_LABEL, PHONE_LABEL, ROLE_LABEL,
    ROLES,
    SAVE_BUTTON_TEXT, SELECT_A_ROLE_TEXT, USER_DETAILS_HEADING, USERNAME_LABEL
} from '../constants/userConstants';

import React, {useState, useEffect, useCallback} from 'react';
import {toast, ToastContainer} from "react-toastify"; // Added ToastContainer here
import 'react-toastify/dist/ReactToastify.css';
import {CLEAR_SUCCESS, SET_SUCCESS} from "../../../app/constants/userActionTypes";
import {
    fetchUser,
    createUser,
    updateUser,
    setFormData,
    setErrors,
    setEditMode as setEditModeAction,
} from "../../../app/actions/userActions";

const UserDetails = () => {
    const navigate = useNavigate();
    const {id} = useParams();
    const dispatch = useDispatch();
    const user = useSelector(state => state.userRequest.userRequest);
    const editModeRedux = useSelector(state => state.userRequest.editMode);
    const [editMode, setEditMode] = useState(editModeRedux);
    const error = useSelector(state => state.userRequest.error);
    const errors = useSelector(state => state.userRequest.errors);
    const success = useSelector(state => state.userRequest.success);
    const loading = useSelector(state => state.userRequest.loading);

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        role: '',
        password: '',
        age: '',
        phone: '',
    });

    useEffect(() => {
        setEditMode(editModeRedux);
    }, [editModeRedux]);

    useEffect(() => {
        if (id !== 'new') {
            dispatch(fetchUser(id));
        } else {
            setEditMode(true);
        }
    }, [id, dispatch]);

    const handleInputChange = useCallback((event) => {
        const {name, value} = event.target;
        setFormData(prevState => ({...prevState, [name]: value}));

        const newError = validate(name, value);
        if (newError[name]) {
            dispatch(setErrors({...errors, [name]: newError[name]}));
        } else {
            const {[name]: value, ...remainingErrors} = errors;
            dispatch(setErrors(remainingErrors));
        }
    }, [dispatch, errors]);

    const handleEdit = () => {
        setFormData({
            username: user.username || '',
            email: user.email || '',
            role: user.role || '',
            password: user.password || '',
            age: user.age || '',
            phone: user.phone || '',
        });
        dispatch(setEditModeAction(true));
    };

    const handleSave = (event) => {
        event.preventDefault();

        const newErrors = validate(formData);
        if (Object.values(newErrors).some(error => error !== '')) {
            dispatch(setErrors(newErrors));
            Object.values(newErrors).forEach((error: string) => {
                toast.error(error);
            });
        } else {
            const action = id === 'new' ? createUser(formData) : updateUser(id, formData);
            dispatch(action).then(() => {
                toast.success(`User ${id === 'new' ? 'created' : 'updated'} successfully`);
                dispatch({type: SET_SUCCESS, payload: `User ${id === 'new' ? 'created' : 'updated'} successfully`});
                setTimeout(() => dispatch({type: CLEAR_SUCCESS}), 5000);
                if (id !== 'new') {
                    dispatch(fetchUser(id));
                }
                setTimeout(() => dispatch({type: CLEAR_SUCCESS}), 5000);
                if (id === 'new') {
                    setTimeout(() => navigate('/user-list'), 5000);
                }
            }).catch((error) => {
                toast.error(`Failed to ${id === 'new' ? 'create' : 'update'} user: ${error.message}`);
            });
        }
    };

    const handleCancel = () => {
        setFormData(user);
        dispatch(setEditModeAction(false));
    };

    const handleBack = () => {
        navigate('/user-list');
    };

    return (
        <div className="container py-5">
            <ToastContainer/>
            <h1 className="my-4 text-center">{USER_DETAILS_HEADING}</h1>
            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}
            {loading ? (
                <p>{LOADING_USER_DATA_TEXT}</p>
            ) : (
                editMode ? (
                    <div className="card shadow">
                        <div className="card-body">
                            <form>
                                <div className="form-group">
                                    <label>{USERNAME_LABEL}</label>
                                    <input type="text" name="username" value={formData.username}
                                           onChange={handleInputChange}
                                           className="form-control"
                                           required/>
                                    {errors.username && <p className="text-danger">{errors.username}</p>}
                                </div>
                                <div className="form-group">
                                    <label>{EMAIL_LABEL}</label>
                                    <input type="email" name="email" value={formData.email} onChange={handleInputChange}
                                           className="form-control"
                                           required/>
                                    {errors.email && <p className="text-danger">{errors.email}</p>}
                                </div>
                                <div className="form-group">
                                    <label>{ROLE_LABEL}</label>
                                    <select name="role" value={formData.role} onChange={handleInputChange}
                                            className="form-control" required>
                                        <option value="" disabled={formData.role !== ''}>{SELECT_A_ROLE_TEXT}</option>
                                        {ROLES.map((role) => (
                                            <option key={role} value={role}>{role}</option>
                                        ))}
                                    </select>
                                    {errors.role && <p className="text-danger">{errors.role}</p>}
                                </div>
                                <div className="form-group">
                                    <label>{PASSWORD_LABEL}</label>
                                    <input type="password" name="password" value={formData.password}
                                           onChange={handleInputChange}
                                           className="form-control"
                                           required/>
                                    {errors.password && <p className="text-danger">{errors.password}</p>}
                                </div>
                                <div className="form-group">
                                    <label>{AGE_LABEL}</label>
                                    <input type="text" name="age" value={formData.age} onChange={handleInputChange}
                                           className="form-control"
                                           required/>
                                    {errors.age && <p className="text-danger">{errors.age}</p>}
                                </div>
                                <div className="form-group">
                                    <label>{PHONE_LABEL}</label>
                                    <input type="text" name="phone" value={formData.phone} onChange={handleInputChange}
                                           className="form-control"
                                           required/>
                                    {errors.phone && <p className="text-danger">{errors.phone}</p>}
                                </div>
                                <button
                                    className={`btn btn-primary ${Object.values(errors).some(error => error !== '') ? 'disabled' : ''}`}
                                    onClick={handleSave}
                                    disabled={Object.values(errors).some(error => error !== '')}>
                                    {id === 'new' ? CREATE_BUTTON_TEXT : SAVE_BUTTON_TEXT}
                                </button>
                                <button onClick={handleCancel}
                                        className="btn btn-secondary ml-2">{CANCEL_BUTTON_TEXT}</button>
                            </form>
                        </div>
                    </div>
                ) : (user && (
                        <div className="card shadow">
                            <div className="card-body">
                                <p>Username: {user.username}</p>
                                <p>Email: {user.email}</p>
                                <p>Password: {user.password ? user.password : NO_PASSWORD_SET_TEXT}</p>
                                <p>Role: {user.role.name}</p>
                                <p>Age: {user.age ? user.age : NO_AGE_SET_TEXT}</p>
                                <p>Phone: {user.phone ? user.phone : NO_PHONE_NUMBER_SET_TEXT}</p>
                                <button onClick={handleEdit} className="btn btn-primary"><FontAwesomeIcon
                                    icon={faEdit}/></button>
                                <button onClick={handleBack}
                                        className="btn btn-secondary ml-2">{BACK_BUTTON_TEXT}</button>
                            </div>
                        </div>
                    )
                ))}
        </div>
    );
}

export default UserDetails;