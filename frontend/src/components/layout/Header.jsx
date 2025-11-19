import React from 'react';
import { GraduationCap } from 'lucide-react';

const Header = () => {
  return (
    <header className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-3">
            <GraduationCap className="h-8 w-8 text-primary-600" />
            <div>
              <h1 className="text-xl font-bold text-gray-900">Closed CRAS</h1>
              <p className="text-xs text-gray-500">Sistema de Agendamento de Recursos</p>
            </div>
          </div>
          <div className="flex items-center space-x-4">
            <span className="text-sm text-gray-600">PUCRS - Construção de Software</span>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
