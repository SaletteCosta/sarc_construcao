import React, { useState, useEffect } from 'react';
import { Plus, Calendar } from 'lucide-react';
import Table from '../../components/common/Table';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { reservationService } from '../../services/reservationService';
import { userService } from '../../services/userService';

const ReservationsPage = () => {
  const [reservations, setReservations] = useState([]);
  const [items, setItems] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    code: '',
    userId: '',
    itemId: '',
    schedule: '',
    reservationDate: '',
    status: 'ACTIVE',
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [reservationsData, itemsData, usersData] = await Promise.all([
        reservationService.getAllReservations(),
        reservationService.getAllItems(),
        userService.getAllUsers(),
      ]);
      setReservations(reservationsData);
      setItems(itemsData);
      setUsers(usersData);
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
      const payload = {
        ...formData,
        userId: parseInt(formData.userId),
        itemId: parseInt(formData.itemId),
      };
      await reservationService.createReservation(payload);
      await fetchData();
      handleCloseModal();
    } catch (error) {
      console.error('Error saving reservation:', error);
      alert('Erro ao salvar reserva');
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setFormData({
      code: '',
      userId: '',
      itemId: '',
      schedule: '',
      reservationDate: '',
      status: 'ACTIVE',
    });
  };

  const getUserName = (userId) => {
    const user = users.find((u) => u.id === userId);
    return user ? user.name : 'N/A';
  };

  const getItemName = (itemId) => {
    const item = items.find((i) => i.id === itemId);
    return item ? item.name : 'N/A';
  };

  const getStatusBadge = (status) => {
    const badges = {
      ACTIVE: 'bg-green-100 text-green-800',
      CANCELLED: 'bg-red-100 text-red-800',
      COMPLETED: 'bg-gray-100 text-gray-800',
    };
    return (
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${badges[status]}`}>
        {status}
      </span>
    );
  };

  const columns = [
    { header: 'Código', accessor: 'code' },
    {
      header: 'Usuário',
      render: (row) => getUserName(row.userId),
    },
    {
      header: 'Item',
      render: (row) => getItemName(row.itemId),
    },
    { header: 'Horário', accessor: 'schedule' },
    {
      header: 'Data',
      render: (row) => new Date(row.reservationDate).toLocaleDateString('pt-BR'),
    },
    {
      header: 'Status',
      render: (row) => getStatusBadge(row.status),
    },
  ];

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Reservas</h1>
        <button
          onClick={() => setIsModalOpen(true)}
          className="btn-primary flex items-center space-x-2"
        >
          <Plus className="h-5 w-5" />
          <span>Nova Reserva</span>
        </button>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div className="card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600">Total de Reservas</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {reservations.length}
                  </p>
                </div>
                <Calendar className="h-8 w-8 text-primary-600" />
              </div>
            </div>
            <div className="card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600">Reservas Ativas</p>
                  <p className="text-2xl font-bold text-green-600">
                    {reservations.filter((r) => r.status === 'ACTIVE').length}
                  </p>
                </div>
                <Calendar className="h-8 w-8 text-green-600" />
              </div>
            </div>
            <div className="card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600">Reservas Concluídas</p>
                  <p className="text-2xl font-bold text-gray-600">
                    {reservations.filter((r) => r.status === 'COMPLETED').length}
                  </p>
                </div>
                <Calendar className="h-8 w-8 text-gray-600" />
              </div>
            </div>
          </div>
          <Table columns={columns} data={reservations} />
        </div>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title="Nova Reserva"
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
              Usuário
            </label>
            <select
              value={formData.userId}
              onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
              className="input-field"
              required
            >
              <option value="">Selecione um usuário</option>
              {users.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.registration} - {user.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Item/Recurso
            </label>
            <select
              value={formData.itemId}
              onChange={(e) => setFormData({ ...formData, itemId: e.target.value })}
              className="input-field"
              required
            >
              <option value="">Selecione um item</option>
              {items
                .filter((item) => item.available)
                .map((item) => (
                  <option key={item.id} value={item.id}>
                    {item.code} - {item.name}
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
              onChange={(e) =>
                setFormData({ ...formData, schedule: e.target.value })
              }
              className="input-field"
              placeholder="MON-08, TUE-10, etc."
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Data da Reserva
            </label>
            <input
              type="date"
              value={formData.reservationDate}
              onChange={(e) =>
                setFormData({ ...formData, reservationDate: e.target.value })
              }
              className="input-field"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Status
            </label>
            <select
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value })}
              className="input-field"
              required
            >
              <option value="ACTIVE">Ativa</option>
              <option value="CANCELLED">Cancelada</option>
              <option value="COMPLETED">Concluída</option>
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
              Criar
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default ReservationsPage;
