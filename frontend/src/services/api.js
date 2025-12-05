import axios from 'axios';

// API Gateway URL - centralized access point
const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:8080';

// Admin Service routes through API Gateway
export const adminApi = axios.create({
  baseURL: `${API_GATEWAY_URL}/api/admin`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// User Service routes through API Gateway
export const userApi = axios.create({
  baseURL: `${API_GATEWAY_URL}/api/user`,
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
