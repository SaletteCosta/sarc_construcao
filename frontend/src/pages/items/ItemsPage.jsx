import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import Table from '../../components/common/Table';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { reservationService } from '../../services/reservationService';

const ItemsPage = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    code: '',
    type: 'LABORATORY',
    name: '',
    available: true,
  });
  const [editingId, setEditingId] = useState(null);
  const [filterType, setFilterType] = useState('ALL');

  useEffect(() => {
    fetchItems();
  }, [filterType]);

  const fetchItems = async () => {
    try {
      setLoading(true);
      let data;
      if (filterType === 'ALL') {
        data = await reservationService.getAllItems();
      } else {
        data = await reservationService.getItemsByType(filterType);
      }
      setItems(data);
    } catch (error) {
      console.error('Error fetching items:', error);
      alert('Erro ao carregar itens');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await reservationService.updateItem(editingId, formData);
      } else {
        await reservationService.createItem(formData);
      }
      await fetchItems();
      handleCloseModal();
    } catch (error) {
      console.error('Error saving item:', error);
      alert('Erro ao salvar item');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este item?')) {
      try {
        await reservationService.deleteItem(id);
        await fetchItems();
      } catch (error) {
        console.error('Error deleting item:', error);
        alert('Erro ao excluir item');
      }
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setFormData({
      code: item.code,
      type: item.type,
      name: item.name,
      available: item.available,
    });
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingId(null);
    setFormData({ code: '', type: 'LABORATORY', name: '', available: true });
  };

  const getItemTypeBadge = (type) => {
    const badges = {
      LABORATORY: 'bg-blue-100 text-blue-800',
      PERIPHERAL: 'bg-green-100 text-green-800',
      ROOM: 'bg-purple-100 text-purple-800',
      EQUIPMENT: 'bg-yellow-100 text-yellow-800',
    };
    return (
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${badges[type]}`}>
        {type}
      </span>
    );
  };

  const getAvailabilityBadge = (available) => {
    return (
      <span
        className={`px-2 py-1 rounded-full text-xs font-medium ${
          available
            ? 'bg-green-100 text-green-800'
            : 'bg-red-100 text-red-800'
        }`}
      >
        {available ? 'Disponível' : 'Indisponível'}
      </span>
    );
  };

  const columns = [
    { header: 'Código', accessor: 'code' },
    { header: 'Nome', accessor: 'name' },
    {
      header: 'Tipo',
      render: (row) => getItemTypeBadge(row.type),
    },
    {
      header: 'Status',
      render: (row) => getAvailabilityBadge(row.available),
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
        <h1 className="text-3xl font-bold text-gray-900">Recursos</h1>
        <div className="flex space-x-4">
          <select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
          >
            <option value="ALL">Todos</option>
            <option value="LABORATORY">Laboratórios</option>
            <option value="PERIPHERAL">Periféricos</option>
            <option value="ROOM">Salas</option>
            <option value="EQUIPMENT">Equipamentos</option>
          </select>
          <button
            onClick={() => setIsModalOpen(true)}
            className="btn-primary flex items-center space-x-2"
          >
            <Plus className="h-5 w-5" />
            <span>Novo Recurso</span>
          </button>
        </div>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <Table columns={columns} data={items} />
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingId ? 'Editar Recurso' : 'Novo Recurso'}
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
              disabled={editingId !== null}
            />
          </div>
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
              Tipo
            </label>
            <select
              value={formData.type}
              onChange={(e) => setFormData({ ...formData, type: e.target.value })}
              className="input-field"
              required
            >
              <option value="LABORATORY">Laboratório</option>
              <option value="PERIPHERAL">Periférico</option>
              <option value="ROOM">Sala</option>
              <option value="EQUIPMENT">Equipamento</option>
            </select>
          </div>
          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              id="available"
              checked={formData.available}
              onChange={(e) =>
                setFormData({ ...formData, available: e.target.checked })
              }
              className="h-4 w-4 text-primary-600 border-gray-300 rounded focus:ring-primary-500"
            />
            <label htmlFor="available" className="text-sm font-medium text-gray-700">
              Disponível
            </label>
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

export default ItemsPage;
