import { create } from 'zustand';

interface AuthState {
  isAuthenticated: boolean;
  token: string | null;
  tenantId: string | null;
  login: (token: string) => void;
  logout: () => void;
  setTenant: (tenantId: string) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,
  token: null,
  tenantId: null,
  login: (token) => set({ isAuthenticated: true, token }),
  logout: () => set({ isAuthenticated: false, token: null, tenantId: null }),
  setTenant: (tenantId) => set({ tenantId }),
}));
