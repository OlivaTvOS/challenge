import { useContext } from 'react';
import { AppDataContext } from './appDataContext';

export function useAppData() {
  const contexto = useContext(AppDataContext);
  if (!contexto) throw new Error('useAppData precisa estar dentro de AppDataProvider');
  return contexto;
}
