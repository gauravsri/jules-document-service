import { useAuthStore } from '../stores/useAuthStore';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
    sub: string; // Subject, typically the username
    roles?: string[];
    exp: number;
}

export const useAuth = () => {
    const token = useAuthStore((state) => state.token);
    const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

    if (!token) {
        return { isAuthenticated: false, user: null, roles: [] };
    }

    try {
        const decoded = jwtDecode<DecodedToken>(token);
        const user = {
            username: decoded.sub,
            roles: decoded.roles || [],
        };
        return { isAuthenticated, user, roles: user.roles };
    } catch (error) {
        console.error("Invalid token:", error);
        return { isAuthenticated: false, user: null, roles: [] };
    }
};
