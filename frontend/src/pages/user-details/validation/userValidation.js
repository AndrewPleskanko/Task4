export const validate = (formData) => {
    let errors = {};

    if (!formData.username) {
        errors.username = "Username is required";
    } else if (formData.username.length < 3) {
        errors.username = "Username must be at least 3 characters long";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email) {
        errors.email = "Email is required";
    } else if (!emailRegex.test(formData.email)) {
        errors.email = "Email is not valid";
    }

    if (!formData.password) {
        errors.password = "Password is required";
    } else if (formData.password.length < 6) {
        errors.password = "Password must be at least 6 characters long";
    }

    if (!formData.role) {
        errors.role = "Role is required";
    }

    if (!formData.age) {
        errors.age = "Age is required";
    } else if (isNaN(formData.age)) {
        errors.age = "Age must be a number";
    } else if (formData.age < 1 || formData.age > 100) {
        errors.age = "Age must be between 1 and 100";
    }

    if (!formData.phone) {
        errors.phone = "Phone number is required";
    } else if (!/^\d+$/.test(formData.phone)) {
        errors.phone = "Phone number must be a number";
    } else if (formData.phone.length < 10 || formData.phone.length > 15) {
        errors.phone = "Phone number must be between 10 and 15 digits long";
    }

    return errors;
};