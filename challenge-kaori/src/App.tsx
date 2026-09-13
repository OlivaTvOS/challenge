import { BrowserRouter } from 'react-router-dom';
import { AppDataProvider } from './core/AppDataContext';
import AppRoutes from './routes/AppRoutes';

// Componente principal — envolve a app com o Provider de dados e o Router
function App() {
  return (
    <AppDataProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </AppDataProvider>
  );
}

export default App;
