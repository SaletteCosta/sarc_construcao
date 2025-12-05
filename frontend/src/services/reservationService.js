import { userApi } from './api';

export const reservationService = {
  // Get all reservations
  getAllReservations: async () => {
    const response = await userApi.get('/reservations');
    return response.data;
  },

  // Get reservation by code
  getReservationByCode: async (code) => {
    const response = await userApi.get(`/reservations/code/${code}`);
    return response.data;
  },

  // Get reservations by schedule
  getReservationsBySchedule: async (schedule) => {
    const response = await userApi.get(`/reservations/schedule/${schedule}`);
    return response.data;
  },

  // Get reservations by user
  getReservationsByUser: async (userId) => {
    const response = await userApi.get(`/reservations/user?userId=${userId}`);
    return response.data;
  },

  // Get reservation schedule
  getReservationSchedule: async (code) => {
    const response = await userApi.get(`/reservations/code/${code}/schedule`);
    return response.data;
  },

  // Get items by type
  getItemsByType: async (type) => {
    const response = await userApi.get(`/reservations/items/type?type=${type}`);
    return response.data;
  },

  // Get laboratories by student registration
  getLaboratoriesByStudent: async (registration) => {
    const response = await userApi.get(`/reservations/student/${registration}/laboratories`);
    return response.data;
  },

  // Create reservation
  createReservation: async (reservationData) => {
    const response = await userApi.post('/reservations', reservationData);
    return response.data;
  },

  // Create peripheral reservation
  createPeripheralReservation: async (reservationData) => {
    const response = await userApi.post('/reservations/peripheral', reservationData);
    return response.data;
  },

  // Get all items
  getAllItems: async () => {
    const response = await userApi.get('/items');
    return response.data;
  },

  // Get item by code
  getItemByCode: async (code) => {
    const response = await userApi.get(`/items/code/${code}`);
    return response.data;
  },

  // Create item
  createItem: async (itemData) => {
    const response = await userApi.post('/items', itemData);
    return response.data;
  },

  // Update item
  updateItem: async (id, itemData) => {
    const response = await userApi.put(`/items/${id}`, itemData);
    return response.data;
  },

  // Delete item
  deleteItem: async (id) => {
    const response = await userApi.delete(`/items/${id}`);
    return response.data;
  },
};
