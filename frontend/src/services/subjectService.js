import { adminApi } from './api';

export const subjectService = {
  // Get all subjects
  getAllSubjects: async () => {
    const response = await adminApi.get('/subjects');
    return response.data;
  },

  // Get subject by code
  getSubjectByCode: async (code) => {
    const response = await adminApi.get(`/subjects/code/${code}`);
    return response.data;
  },

  // Create new subject
  createSubject: async (subjectData) => {
    const response = await adminApi.post('/subjects', subjectData);
    return response.data;
  },

  // Update subject
  updateSubject: async (id, subjectData) => {
    const response = await adminApi.put(`/subjects/${id}`, subjectData);
    return response.data;
  },

  // Delete subject
  deleteSubject: async (id) => {
    const response = await adminApi.delete(`/subjects/${id}`);
    return response.data;
  },
};
