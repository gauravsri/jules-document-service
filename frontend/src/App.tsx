import { Link, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import LoginPage from './components/LoginPage';
import ProtectedRoute from './components/ProtectedRoute';
import CaseDetailPage from './pages/CaseDetailPage';
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
                <nav>
                  <Link to="/cases/1">View Sample Case 1</Link>
                </nav>
              </div>
            } />
            <Route path="/cases/:caseId" element={<CaseDetailPage />} />
        </Route>
      </Route>
    </Routes>
  );
}

export default App;
