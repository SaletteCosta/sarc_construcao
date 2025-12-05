import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  BookOpen,
  GraduationCap,
  Users,
  Laptop,
  Calendar,
  TrendingUp,
} from 'lucide-react';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { subjectService } from '../../services/subjectService';
import { classService } from '../../services/classService';
import { userService } from '../../services/userService';
import { reservationService } from '../../services/reservationService';

const DashboardPage = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    subjects: 0,
    classes: 0,
    users: 0,
    items: 0,
    reservations: 0,
    activeReservations: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const [subjects, classes, users, items, reservations] = await Promise.all([
        subjectService.getAllSubjects(),
        classService.getAllClasses(),
        userService.getAllUsers(),
        reservationService.getAllItems(),
        reservationService.getAllReservations(),
      ]);

      setStats({
        subjects: subjects.length,
        classes: classes.length,
        users: users.length,
        items: items.length,
        reservations: reservations.length,
        activeReservations: reservations.filter((r) => r.status === 'ACTIVE').length,
      });
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const statsCards = [
    {
      title: 'Disciplinas',
      value: stats.subjects,
      icon: BookOpen,
      color: 'bg-blue-500',
      path: '/subjects',
    },
    {
      title: 'Turmas',
      value: stats.classes,
      icon: GraduationCap,
      color: 'bg-green-500',
      path: '/classes',
    },
    {
      title: 'Usuários',
      value: stats.users,
      icon: Users,
      color: 'bg-purple-500',
      path: '/users',
    },
    {
      title: 'Recursos',
      value: stats.items,
      icon: Laptop,
      color: 'bg-yellow-500',
      path: '/items',
    },
    {
      title: 'Reservas Totais',
      value: stats.reservations,
      icon: Calendar,
      color: 'bg-red-500',
      path: '/reservations',
    },
    {
      title: 'Reservas Ativas',
      value: stats.activeReservations,
      icon: TrendingUp,
      color: 'bg-indigo-500',
      path: '/reservations',
    },
  ];

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600 mt-2">
          Visão geral do Sistema de Agendamento de Recursos Acadêmicos
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {statsCards.map((card, index) => {
          const Icon = card.icon;
          return (
            <div
              key={index}
              onClick={() => navigate(card.path)}
              className="card hover:shadow-lg transition-shadow cursor-pointer"
            >
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">{card.title}</p>
                  <p className="text-3xl font-bold text-gray-900">{card.value}</p>
                </div>
                <div className={`${card.color} p-3 rounded-lg`}>
                  <Icon className="h-8 w-8 text-white" />
                </div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-4">
            Ações Rápidas
          </h2>
          <div className="space-y-3">
            <button
              onClick={() => navigate('/subjects')}
              className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <div className="flex items-center space-x-3">
                <BookOpen className="h-5 w-5 text-primary-600" />
                <span className="font-medium">Gerenciar Disciplinas</span>
              </div>
            </button>
            <button
              onClick={() => navigate('/classes')}
              className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <div className="flex items-center space-x-3">
                <GraduationCap className="h-5 w-5 text-primary-600" />
                <span className="font-medium">Gerenciar Turmas</span>
              </div>
            </button>
            <button
              onClick={() => navigate('/reservations')}
              className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <div className="flex items-center space-x-3">
                <Calendar className="h-5 w-5 text-primary-600" />
                <span className="font-medium">Nova Reserva</span>
              </div>
            </button>
            <button
              onClick={() => navigate('/items')}
              className="w-full text-left px-4 py-3 bg-gray-50 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <div className="flex items-center space-x-3">
                <Laptop className="h-5 w-5 text-primary-600" />
                <span className="font-medium">Gerenciar Recursos</span>
              </div>
            </button>
          </div>
        </div>

        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-4">
            Sobre o Sistema
          </h2>
          <div className="space-y-4 text-gray-700">
            <p>
              O <strong>Closed CRAS</strong> é um sistema completo de
              gerenciamento de recursos acadêmicos desenvolvido para facilitar o
              agendamento de laboratórios, salas, equipamentos e periféricos.
            </p>
            <div className="border-t pt-4">
              <h3 className="font-semibold mb-2">Funcionalidades:</h3>
              <ul className="list-disc list-inside space-y-1 text-sm">
                <li>Gerenciamento de disciplinas e turmas</li>
                <li>Controle de usuários (alunos, professores, admin)</li>
                <li>Cadastro de recursos (labs, salas, equipamentos)</li>
                <li>Sistema de reservas com controle de horários</li>
                <li>Relatórios e estatísticas em tempo real</li>
              </ul>
            </div>
            <div className="border-t pt-4 text-sm text-gray-600">
              <p>PUCRS - Construção de Software</p>
              <p className="mt-1">Grupo: Eduardo, Nicoli, Ruan, Salette, Vicenzo, Vitória</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
