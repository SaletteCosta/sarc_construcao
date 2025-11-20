import React, { useState, useEffect } from 'react';
import { Plus, Edit, UserPlus, Clock } from 'lucide-react';
import Table from '../../components/common/Table';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { classService } from '../../services/classService';
import { subjectService } from '../../services/subjectService';
import { userService } from '../../services/userService';

const ClassesPage = () => {
  const [classes, setClasses] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddStudentModalOpen, setIsAddStudentModalOpen] = useState(false);
  const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
  const [selectedClass, setSelectedClass] = useState(null);
  const [formData, setFormData] = useState({ code: '', subjectId: '', schedule: '' });
  const [selectedStudentId, setSelectedStudentId] = useState('');
  const [scheduleData, setScheduleData] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [classesData, subjectsData, studentsData] = await Promise.all([
        classService.getAllClasses(),
        subjectService.getAllSubjects(),
        userService.getUsersByType('STUDENT'),
      ]);
      setClasses(classesData);
      setSubjects(subjectsData);
      setStudents(studentsData);
    } catch (error) {
      console.error('Error fetching data:', error);
      alert('Erro ao carregar dados');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await classService.createClass({
        ...formData,
        subjectId: parseInt(formData.subjectId),
      });
      await fetchData();
      handleCloseModal();
    } catch (error) {
      console.error('Error saving class:', error);
      alert('Erro ao salvar turma');
    }
  };

  const handleAddStudent = async (e) => {
    e.preventDefault();
    try {
      await classService.addStudentToClass(selectedClass.code, parseInt(selectedStudentId));
      alert('Aluno adicionado com sucesso!');
      setIsAddStudentModalOpen(false);
      setSelectedStudentId('');
    } catch (error) {
      console.error('Error adding student:', error);
      alert('Erro ao adicionar aluno à turma');
    }
  };

  const handleUpdateSchedule = async (e) => {
    e.preventDefault();
    try {
      await classService.updateSchedule(selectedClass.code, scheduleData);
      await fetchData();
      setIsScheduleModalOpen(false);
      setScheduleData('');
      alert('Horário atualizado com sucesso!');
    } catch (error) {
      console.error('Error updating schedule:', error);
      alert('Erro ao atualizar horário');
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setFormData({ code: '', subjectId: '', schedule: '' });
  };

  const getSubjectName = (subjectId) => {
    const subject = subjects.find((s) => s.id === subjectId);
    return subject ? subject.name : 'N/A';
  };

  const columns = [
    { header: 'Código', accessor: 'code' },
    {
      header: 'Disciplina',
      render: (row) => getSubjectName(row.subjectId),
    },
    { header: 'Horário', accessor: 'schedule' },
    {
      header: 'Ações',
      render: (row) => (
        <div className="flex space-x-2">
          <button
            onClick={(e) => {
              e.stopPropagation();
              setSelectedClass(row);
              setScheduleData(row.schedule || '');
              setIsScheduleModalOpen(true);
            }}
            className="text-blue-600 hover:text-blue-800"
            title="Atualizar Horário"
          >
            <Clock className="h-4 w-4" />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              setSelectedClass(row);
              setIsAddStudentModalOpen(true);
            }}
            className="text-green-600 hover:text-green-800"
            title="Adicionar Aluno"
          >
            <UserPlus className="h-4 w-4" />
          </button>
        </div>
      ),
    },
  ];

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Turmas</h1>
        <button
          onClick={() => setIsModalOpen(true)}
          className="btn-primary flex items-center space-x-2"
        >
          <Plus className="h-5 w-5" />
          <span>Nova Turma</span>
        </button>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <Table columns={columns} data={classes} />
      )}

      {/* Create Class Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title="Nova Turma"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Código
            </label>
            <input
              type="text"
              value={formData.code}
              onChange={(e) => setFormData({ ...formData, code: e.target.value })}
              className="input-field"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Disciplina
            </label>
            <select
              value={formData.subjectId}
              onChange={(e) => setFormData({ ...formData, subjectId: e.target.value })}
              className="input-field"
              required
            >
              <option value="">Selecione uma disciplina</option>
              {subjects.map((subject) => (
                <option key={subject.id} value={subject.id}>
                  {subject.code} - {subject.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Horário (ex: MON-08)
            </label>
            <input
              type="text"
              value={formData.schedule}
              onChange={(e) => setFormData({ ...formData, schedule: e.target.value })}
              className="input-field"
              placeholder="MON-08, TUE-10, etc."
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <button
              type="button"
              onClick={handleCloseModal}
              className="btn-secondary"
            >
              Cancelar
            </button>
            <button type="submit" className="btn-primary">
              Criar
            </button>
          </div>
        </form>
      </Modal>

      {/* Add Student Modal */}
      <Modal
        isOpen={isAddStudentModalOpen}
        onClose={() => {
          setIsAddStudentModalOpen(false);
          setSelectedStudentId('');
        }}
        title={`Adicionar Aluno - ${selectedClass?.code}`}
      >
        <form onSubmit={handleAddStudent} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Aluno
            </label>
            <select
              value={selectedStudentId}
              onChange={(e) => setSelectedStudentId(e.target.value)}
              className="input-field"
              required
            >
              <option value="">Selecione um aluno</option>
              {students.map((student) => (
                <option key={student.id} value={student.id}>
                  {student.registration} - {student.name}
                </option>
              ))}
            </select>
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <button
              type="button"
              onClick={() => {
                setIsAddStudentModalOpen(false);
                setSelectedStudentId('');
              }}
              className="btn-secondary"
            >
              Cancelar
            </button>
            <button type="submit" className="btn-primary">
              Adicionar
            </button>
          </div>
        </form>
      </Modal>

      {/* Update Schedule Modal */}
      <Modal
        isOpen={isScheduleModalOpen}
        onClose={() => {
          setIsScheduleModalOpen(false);
          setScheduleData('');
        }}
        title={`Atualizar Horário - ${selectedClass?.code}`}
      >
        <form onSubmit={handleUpdateSchedule} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Horário (ex: MON-08)
            </label>
            <input
              type="text"
              value={scheduleData}
              onChange={(e) => setScheduleData(e.target.value)}
              className="input-field"
              placeholder="MON-08, TUE-10, etc."
              required
            />
          </div>
          <div className="flex justify-end space-x-2 pt-4">
            <button
              type="button"
              onClick={() => {
                setIsScheduleModalOpen(false);
                setScheduleData('');
              }}
              className="btn-secondary"
            >
              Cancelar
            </button>
            <button type="submit" className="btn-primary">
              Atualizar
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default ClassesPage;
