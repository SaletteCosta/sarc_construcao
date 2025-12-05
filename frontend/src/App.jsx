import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/layout/Layout';
import DashboardPage from './pages/dashboard/DashboardPage';
import SubjectsPage from './pages/subjects/SubjectsPage';
import ClassesPage from './pages/classes/ClassesPage';
import UsersPage from './pages/users/UsersPage';
import ItemsPage from './pages/items/ItemsPage';
import ReservationsPage from './pages/reservations/ReservationsPage';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/subjects" element={<SubjectsPage />} />
          <Route path="/classes" element={<ClassesPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/items" element={<ItemsPage />} />
          <Route path="/reservations" element={<ReservationsPage />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
