import { create } from 'zustand';

interface AuthState {
  isAuthenticated: boolean;
  tenantId: string | null;
  login: () => void;
  logout: () => void;
  setTenant: (tenantId: string) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,
  tenantId: null,
  login: () => set({ isAuthenticated: true }),
  logout: () => set({ isAuthenticated: false, tenantId: null }),
  setTenant: (tenantId) => set({ tenantId }),
}));
