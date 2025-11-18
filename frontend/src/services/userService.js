import { userApi } from './api';

export const userService = {
  // Get all users
  getAllUsers: async () => {
    const response = await userApi.get('/users');
    return response.data;
  },

  // Get user by registration
  getUserByRegistration: async (registration) => {
    const response = await userApi.get(`/users/registration/${registration}`);
    return response.data;
  },

  // Get users by type
  getUsersByType: async (type) => {
    const response = await userApi.get(`/users/type/${type}`);
    return response.data;
  },

  // Create new user
  createUser: async (userData) => {
    const response = await userApi.post('/users', userData);
    return response.data;
  },

  // Update user
  updateUser: async (id, userData) => {
    const response = await userApi.put(`/users/${id}`, userData);
    return response.data;
  },

  // Delete user
  deleteUser: async (id) => {
    const response = await userApi.delete(`/users/${id}`);
    return response.data;
  },
};
