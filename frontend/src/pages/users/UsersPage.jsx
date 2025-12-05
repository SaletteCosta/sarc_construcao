import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/common/Table';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { userService } from '../../services/userService';

const UsersPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    registration: '',
    type: 'STUDENT',
  });
  const [editingId, setEditingId] = useState(null);
  const [filterType, setFilterType] = useState('ALL');

  useEffect(() => {
    fetchUsers();
  }, [filterType]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      let data;
      if (filterType === 'ALL') {
        data = await userService.getAllUsers();
      } else {
        data = await userService.getUsersByType(filterType);
      }
      setUsers(data);
    } catch (error) {
      console.error('Error fetching users:', error);
      alert('Erro ao carregar usuários');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await userService.updateUser(editingId, formData);
      } else {
        await userService.createUser(formData);
      }
      await fetchUsers();
      handleCloseModal();
    } catch (error) {
      console.error('Error saving user:', error);
      alert('Erro ao salvar usuário');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este usuário?')) {
      try {
        await userService.deleteUser(id);
        await fetchUsers();
      } catch (error) {
        console.error('Error deleting user:', error);
        alert('Erro ao excluir usuário');
      }
    }
  };

  const handleEdit = (user) => {
    setEditingId(user.id);
    setFormData({
      name: user.name,
      registration: user.registration,
      type: user.type,
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingId(null);
    setFormData({ name: '', registration: '', type: 'STUDENT' });
  };

  const getUserTypeBadge = (type) => {
    const badges = {
      STUDENT: 'bg-blue-100 text-blue-800',
      TEACHER: 'bg-green-100 text-green-800',
      ADMIN: 'bg-purple-100 text-purple-800',
    };
    return (
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${badges[type]}`}>
        {type}
      </span>
    );
  };

  const columns = [
    { header: 'Nome', accessor: 'name' },
    { header: 'Matrícula', accessor: 'registration' },
    {
      header: 'Tipo',
      render: (row) => getUserTypeBadge(row.type),
    },
    {
      header: 'Ações',
      render: (row) => (
        <div className="flex space-x-2">
          <button
            onClick={(e) => {
              e.stopPropagation();
              handleEdit(row);
            }}
            className="text-blue-600 hover:text-blue-800"
          >
            <Edit className="h-4 w-4" />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(row.id);
            }}
            className="text-red-600 hover:text-red-800"
          >
            <Trash2 className="h-4 w-4" />
          </button>
        </div>
      ),
    },
  ];

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Usuários</h1>
        <div className="flex space-x-4">
          <select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
          >
            <option value="ALL">Todos</option>
            <option value="STUDENT">Alunos</option>
            <option value="TEACHER">Professores</option>
            <option value="ADMIN">Administradores</option>
          </select>
          <button
            onClick={() => setIsModalOpen(true)}
            className="btn-primary flex items-center space-x-2"
          >
            <Plus className="h-5 w-5" />
            <span>Novo Usuário</span>
          </button>
        </div>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <Table columns={columns} data={users} />
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingId ? 'Editar Usuário' : 'Novo Usuário'}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome
            </label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="input-field"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Matrícula
            </label>
            <input
              type="text"
              value={formData.registration}
              onChange={(e) =>
                setFormData({ ...formData, registration: e.target.value })
              }
              className="input-field"
              required
              disabled={editingId !== null}
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Tipo
            </label>
            <select
              value={formData.type}
              onChange={(e) => setFormData({ ...formData, type: e.target.value })}
              className="input-field"
              required
            >
              <option value="STUDENT">Aluno</option>
              <option value="TEACHER">Professor</option>
              <option value="ADMIN">Administrador</option>
            </select>
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
              {editingId ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default UsersPage;
