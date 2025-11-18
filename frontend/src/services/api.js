import axios from 'axios';

const ADMIN_SERVICE_URL = import.meta.env.VITE_ADMIN_SERVICE_URL || 'http://localhost:8084';
const USER_SERVICE_URL = import.meta.env.VITE_USER_SERVICE_URL || 'http://localhost:8085';

export const adminApi = axios.create({
  baseURL: ADMIN_SERVICE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const userApi = axios.create({
  baseURL: USER_SERVICE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptors for error handling
const handleError = (error) => {
  if (error.response) {
    console.error('Response error:', error.response.data);
    throw new Error(error.response.data.message || 'An error occurred');
  } else if (error.request) {
    console.error('Request error:', error.request);
    throw new Error('No response from server');
  } else {
    console.error('Error:', error.message);
    throw error;
  }
};

adminApi.interceptors.response.use(
  (response) => response,
  (error) => handleError(error)
);

userApi.interceptors.response.use(
  (response) => response,
  (error) => handleError(error)
);
