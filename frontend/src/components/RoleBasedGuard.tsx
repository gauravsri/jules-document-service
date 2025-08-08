import React from 'react';
import { useAuth } from '../hooks/useAuth';

interface RoleBasedGuardProps {
    allowedRoles: string[];
    children: React.ReactNode;
}

export default function RoleBasedGuard({ allowedRoles, children }: RoleBasedGuardProps) {
    const { user } = useAuth();

    const hasRequiredRole = user?.roles.some(role => allowedRoles.includes(role));

    if (!hasRequiredRole) {
        return null; // Or render a "Forbidden" component
    }

    return <>{children}</>;
}
