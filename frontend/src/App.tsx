import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import LoginPage from './components/LoginPage';
import ProtectedRoute from './components/ProtectedRoute';
import { useAuthStore } from './stores/useAuthStore';

function App() {

  const tenantId = useAuthStore((state) => state.tenantId);

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<Layout />}>
            {/* Nested routes for the main app will go here */}
            <Route index element={
              <div>
                <h2>Welcome to the Dashboard</h2>
                {tenantId && <p>Current Tenant: <strong>{tenantId}</strong></p>}
              </div>
            } />
        </Route>
      </Route>
    </Routes>
  );
}

export default App;
