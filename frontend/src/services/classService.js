import { adminApi } from './api';

export const classService = {
  // Get all classes
  getAllClasses: async () => {
    const response = await adminApi.get('/classes');
    return response.data;
  },

  // Get class by code
  getClassByCode: async (code) => {
    const response = await adminApi.get(`/classes/code/${code}`);
    return response.data;
  },

  // Get class schedule
  getClassSchedule: async (code) => {
    const response = await adminApi.get(`/classes/code/${code}/schedule`);
    return response.data;
  },

  // Get classes by student
  getClassesByStudent: async (studentId) => {
    const response = await adminApi.get(`/classes/student/${studentId}`);
    return response.data;
  },

  // Create new class
  createClass: async (classData) => {
    const response = await adminApi.post('/classes', classData);
    return response.data;
  },

  // Add student to class
  addStudentToClass: async (code, studentId) => {
    const response = await adminApi.post(`/classes/code/${code}/students/${studentId}`);
    return response.data;
  },

  // Update class schedule
  updateSchedule: async (code, schedule) => {
    const response = await adminApi.post(`/classes/code/${code}/schedule`, { schedule });
    return response.data;
  },
};
