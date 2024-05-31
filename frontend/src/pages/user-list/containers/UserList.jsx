import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useNavigate,useLocation} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {ToastContainer} from 'react-toastify';
import {USER_DETAIL_PAGE, USER_DETAIL_NEW_PAGE} from '../../../app/constants/apiConstants';
import {
    ADD_USER_BUTTON_TEXT,
    CONFIRM_DELETE_MESSAGE,
    EMAIL_FILTER_PLACEHOLDER, ERROR_DELETE_TOAST,
    NEXT_BUTTON_TEXT,
    PREVIOUS_BUTTON_TEXT, SUCCESS_DELETE_TOAST,
    USERNAME_FILTER_PLACEHOLDER,
    USERS_HEADING,
} from '../constants/messageConstants';
import {
    fetchUsers,
    deleteUser,
    setFilter,
    setPage,
    loadSettings,
    saveSettings,

} from "../../../app/actions/userActions";
import {toast} from 'react-toastify';

const UserList = () => {
    const location = useLocation();
    const [showModal, setShowModal] = React.useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {users, loading, message, totalPages, page, filter} = useSelector(state => state.userRequest);
    const {username: usernameFilter, email: emailFilter} = filter;
    const [hoveredUserId, setHoveredUserId] = React.useState(null);

    useEffect(() => {
        dispatch(loadSettings());
    }, [dispatch]);

    useEffect(() => {
        dispatch(saveSettings({filter, page}));
        dispatch(fetchUsers(page, filter));
    }, [dispatch, filter, page]);

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const pageFromUrl = queryParams.get('page');
        const filterFromUrl = queryParams.get('filter');

        if (pageFromUrl) {
            dispatch(setPage(Number(pageFromUrl)));
        }

        if (filterFromUrl) {
            dispatch(setFilter(JSON.parse(filterFromUrl)));
        }
    }, [dispatch, location.search]);


    const handleFilterChange = (event) => {
        const newFilter = {...filter, [event.target.name]: event.target.value};
        dispatch(setFilter(newFilter));
        navigate(`${location.pathname}?page=${page}&filter=${JSON.stringify(newFilter)}`);
    };

    const handlePageChange = (newPage) => {
        dispatch(setPage(newPage));
        navigate(`${location.pathname}?page=${newPage}&filter=${JSON.stringify(filter)}`);
    };

    const navigateToUserDetail = (userId) => {
        navigate(`${USER_DETAIL_PAGE}${userId}`);
    };

    const navigateToUserCreation = () => {
        navigate(USER_DETAIL_NEW_PAGE);
    };

    const confirmDelete = (userId) => {
        setShowModal(true);
        if (window.confirm(CONFIRM_DELETE_MESSAGE)) {
            dispatch(deleteUser(userId)).then(() => {
                toast.success(SUCCESS_DELETE_TOAST);
                dispatch(fetchUsers(page, filter));
            }).catch((error) => {
                toast.error(`${ERROR_DELETE_TOAST} ${error.message}`);
            });
        }
    };

    return (
        <div className="container">
            <ToastContainer/>
            <h1>{USERS_HEADING}</h1>
            {message &&
                <p className={message.type === 'error' ? 'alert alert-danger' : 'alert alert-success'}>{message.text}</p>}
            <div className="input-group mb-3">
                <input type="text" name="username" value={usernameFilter} onChange={handleFilterChange}
                       placeholder={USERNAME_FILTER_PLACEHOLDER} className="form-control"/>
                <input type="text" name="email" value={emailFilter} onChange={handleFilterChange}
                       placeholder={EMAIL_FILTER_PLACEHOLDER} className="form-control"/>
                <div className="input-group-append">
                    <button onClick={navigateToUserCreation} className="btn btn-primary">{ADD_USER_BUTTON_TEXT}</button>
                </div>
            </div>
            {users.map((user) => (
                <div key={user.id}
                     onMouseEnter={() => setHoveredUserId(user.id)}
                     onMouseLeave={() => setHoveredUserId(null)}
                     className="card mb-3">
                    <div className="card-body">
                        <h2 className="card-title" onClick={() => navigateToUserDetail(user.id)}>{user.username}</h2>
                        <p className="card-text">Email: {user.email}</p>
                        {hoveredUserId === user.id &&
                            <button onClick={() => confirmDelete(user.id)} className="btn btn-danger">
                                <FontAwesomeIcon icon={faTrash}/>
                            </button>}
                    </div>
                </div>
            ))}
            <div className="d-flex justify-content-between">
                <button onClick={() => handlePageChange(page - 1)} disabled={page === 1}
                        className="btn btn-outline-secondary mr-2">{PREVIOUS_BUTTON_TEXT}
                </button>
                <button className="btn btn-outline-secondary mr-2 disabled">{page}</button>
                <button onClick={() => handlePageChange(page + 1)} className="btn btn-outline-secondary"
                        disabled={page === totalPages}>{NEXT_BUTTON_TEXT}
                </button>
            </div>
        </div>
    );
};

export default UserList;